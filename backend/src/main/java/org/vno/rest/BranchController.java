package org.vno.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.domain.Branch;
import org.vno.domain.Commit;
import org.vno.domain.Repo;
import org.vno.repository.BranchRepository;
import org.vno.repository.CommitRepository;
import org.vno.repository.RepoRepository;

import java.util.HashSet;

/**
 * @author kk
 */
@RequestMapping("/ref")
@RestController
@PreAuthorize("isAuthenticated()")
public class BranchController {

    private final RepoController repoController;
    private final BranchRepository branchRepository;
    private final RepoRepository repoRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public BranchController(RepoController repoController,
                            BranchRepository branchRepository,
                            RepoRepository repoRepository,
                            CommitRepository commitRepository) {
        this.repoController = repoController;
        this.branchRepository = branchRepository;
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
    }

    /**
     * Returns branch by its and repo's ids
     * TODO: why doesn't this method have a check for relation between branch
     * TODO: and repo?
     *
     * @param repoId repository id, where branch must be presented
     * @param branchId id of required branch
     * @return branch or 403 (TODO: 404)
     */
    @GetMapping("/{repoId}/{branchId}")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long branchId) {
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Branch b = branchRepository.findOne(branchId);
        if (! b.getRepo().getId().equals(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        b.getHead().setParents(null);
        b.getHead().setAuthor(null);
        b.getHead().setBlobs(null);
        return ResponseEntity.ok(b);
    }

    /**
     * Creates new branch
     *
     * @param repoId repository to store branch
     * @param headId commit to point by branch
     * @param name branch name
     * @return 200 on success, 403 if don't have permissions, 400 if already
     * exists
     */
    @PutMapping("/{repoId}/{headId}/{name}")
    ResponseEntity<?> add(@PathVariable Long repoId, @PathVariable Long headId,
                          @PathVariable String name) {
        Repo r = repoRepository.findOneWithBranches(repoId);
        if (null == r) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (r.getBranches() == null) {
            r.setBranches(new HashSet<>());
        }
        if (r.getBranches().stream().anyMatch(b -> b.getName().equals(name))) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }

        Commit c = null;
        if (! headId.equals(0L)) {
            c = commitRepository.findOne(headId);
            if (null == c) {
                return new ResponseEntity<>("No such revision",
                        HttpStatus.NOT_FOUND);
            }
        }

        Branch branch = new Branch();
        branch.setName(name);
        branch.setBranch(null);
        branch.setHead(c); // TODO: commit in repo?
        branch.setRepo(r);
        branch = branchRepository.save(branch);
        r.getBranches().add(branch);
        repoRepository.save(r);

        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    /**
     * Deletes a branch
     *
     * @param repoId id of repository, which is storing branch
     * @param branchId id of branch
     * @return 403 if not permitted, 200 if deleted
     */
    @DeleteMapping("/{repoId}/{branchId}")
    ResponseEntity<?> del(@PathVariable Long repoId,
                          @PathVariable Long branchId) {
        if (! repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        branchRepository.delete(branchId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Checks branch access for current user
     *
     * @param branchId branch id
     * @return true if user has access to branch with given id
     */
    public boolean hasAccessTo(Long branchId) {
        return repoController
                .hasAccessTo(repoRepository.findByBranchesContains(
                        branchRepository.findOne(branchId)).getId());
    }
}
