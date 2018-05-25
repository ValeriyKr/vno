package org.vno.pg.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.pg.domain.Blob;
import org.vno.pg.domain.Branch;
import org.vno.pg.domain.Commit;
import org.vno.pg.domain.Repo;
import org.vno.pg.repository.BlobRepository;
import org.vno.pg.repository.BranchRepository;
import org.vno.pg.repository.CommitRepository;
import org.vno.pg.repository.RepoRepository;
import org.vno.pg.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author kk
 */
@RequestMapping("/r")
@RestController
@PreAuthorize("isAuthenticated()")
public class RevisionController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final RepoRepository repoRepository;
    private final BranchController branchController;
    private final UserAccountRepository userAccountRepository;
    private final CommitRepository commitRepository;
    private final BranchRepository branchRepository;
    private final BlobRepository blobRepository;

    @Autowired
    public RevisionController(RepoRepository repoRepository,
                              BranchController branchController,
                              UserAccountRepository userAccountRepository,
                              CommitRepository commitRepository,
                              BranchRepository branchRepository,
                              BlobRepository blobRepository) {
        this.repoRepository = repoRepository;
        this.branchController = branchController;
        this.userAccountRepository = userAccountRepository;
        this.commitRepository = commitRepository;
        this.branchRepository = branchRepository;
        this.blobRepository = blobRepository;
    }

    /**
     * Data transferring object for commit
     */
    public static class CommitDto {
        private Commit commit;
        private Set<Blob> blobs;

        public CommitDto() {}

        public Commit getCommit() {
            return commit;
        }

        public void setCommit(Commit commit) {
            this.commit = commit;
        }

        public Set<Blob> getBlobs() {
            return blobs;
        }

        public void setBlobs(Set<Blob> blobs) {
            this.blobs = blobs;
        }
    }

    /**
     * Retrieves commit from branch in repository
     *
     * @param repoId repository id (must be accessible for user)
     * @param branchId branch id in repository
     * @param revision revision number
     * @return commit, 403 or 404
     */
    @GetMapping("/{repoId}/{branchId}/{revision}/")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long branchId,
                          @PathVariable Long revision) {
        Repo r = repoRepository.findOneWithBranches(repoId);
        if (null == r) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (r.getBranches() == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (r.getBranches().stream().noneMatch(
                b -> b.getBranch().equals(branchId))) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (! branchController.hasAccessTo(branchId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Branch b = branchRepository.findOne(branchId);
        if (b.getHead() == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Queue<Commit> queue = new LinkedList<>(
                Collections.singleton(b.getHead()));
        while (! queue.isEmpty()) {
            Commit c = queue.poll();
            if (c == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            if (c.getRevision().equals(revision)) {
                return ResponseEntity.ok(c);
            }
            if (c.getParents() != null) {
                queue.addAll(
                        commitRepository.findAll(
                                c.getParents().stream().mapToLong(
                                        Commit::getRevision
                                ).boxed().collect(Collectors.toList())
                        )
                );
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * Saves commit into repository
     *
     * Example:
     * PUT request to example.com/r/1/1/
     * Request body:
     * {
     *   "commit":{
     *     "message":"Add javadoc",
     *     "timestamp":12345,
     *     "parents":[{"revision":1}, {"revision":4}, {"revision":3}]
     *   },
     *   "blobs":[
     *     {
     *       "name":"CMakeLists.txt",
     *       "mode":13,
     *       "content":"base64encodedcontent"
     *     },
     *     {
     *       "name":"src/main.cxx",
     *       "mode":15,
     *       "content":"base64encodedcontent"
     *     }
     *   ]
     * }
     *
     *
     * @param repoId (see get())
     * @param branchId (see get())
     * @param commitDto (see get())
     * @return (see get())
     */
    @PutMapping("/{repoId}/{branchId}/")
    ResponseEntity<?> add(@PathVariable Long repoId,
                          @PathVariable Long branchId,
                          @RequestBody CommitDto commitDto) {

        Repo r = repoRepository.findOneWithBranches(repoId);
        Optional<Branch> ob = r.getBranches().stream().filter(
                br -> br.getBranch().equals(branchId)).findAny();
        if (! ob.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (! branchController.hasAccessTo(branchId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (null == commitDto.commit || null == commitDto.blobs) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Commit commit = commitDto.getCommit();
        Long bHead = null;
        if (ob.get().getHead() != null) {
            bHead = ob.get().getHead().getRevision();
        }

        if (((commit.getParents() == null || commit.getParents().isEmpty()) &&
                bHead != null) ||
                (commit.getParents() != null && bHead == null)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (commit.getParents() != null && bHead != null) {
            Long finalBHead = bHead;
            if (commit.getParents().stream().noneMatch(
                    c -> c.getRevision().equals(finalBHead))) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        if (null != commit.getParents()) {
            for (Commit c : commit.getParents()) {
                // TODO: check parents in repo
            }
        }

        commit.setRevision(null);
        commit.setAuthor(userAccountRepository.findByUsername(
                SecurityContextHolder.getContext()
                        .getAuthentication().getName()));
        commit.setBlobs(commitDto.blobs);
        commit = commitRepository.save(commit);
        Commit finalCommit = commit;
        commitDto.blobs.forEach(b -> {
            b.setId(null);
            b.setRevision(finalCommit);
        });
        blobRepository.save(commitDto.blobs);
        ob.get().setHead(commit);
        branchRepository.save(ob.get());

        return ResponseEntity.ok(commit);
    }

}
