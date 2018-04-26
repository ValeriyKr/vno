package org.vno.gateway.rest;

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
import org.vno.gateway.bridge.CassandraBridge;
import org.vno.gateway.bridge.MongoBridge;
import org.vno.gateway.bridge.NeoBridge;
import org.vno.gateway.domain.Branch;

/**
 * @author kk
 */
@RequestMapping("/ref")
@RestController
@PreAuthorize("isAuthenticated()")
public class BranchController {

    private final NeoBridge neoBridge;
    private final MongoBridge mongoBridge;
    private final CassandraBridge cassandraBridge;
    private final RepoController repoController;

    @Autowired
    public BranchController(NeoBridge neoBridge,
                            MongoBridge mongoBridge,
                            CassandraBridge cassandraBridge,
                            RepoController repoController) {
        this.neoBridge = neoBridge;
        this.mongoBridge = mongoBridge;
        this.cassandraBridge = cassandraBridge;
        this.repoController = repoController;
        assert null != neoBridge;
        assert null != mongoBridge;
        assert null != cassandraBridge;
        assert null != repoController;
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
        return ResponseEntity.ok(neoBridge.getBranchById(branchId));
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
        Branch branch = new Branch();
        branch.setName(name);
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (null != neoBridge.getBranchByNameWithIdIn(branch.getName(),
                mongoBridge.getRepoById(repoId).getBranchIds())) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        branch.setId(null);
        branch.setHead(headId); // TODO: commit in repo?
        Long id = neoBridge.saveBranch(branch);
        cassandraBridge.addBranch(repoId, id,
                mongoBridge.getCollaborators(repoId));
        return new ResponseEntity(HttpStatus.OK);
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
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        neoBridge.deleteBranch(branchId);
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
                .hasAccessTo(mongoBridge.getRepoByBranch(branchId).getId());
    }
}
