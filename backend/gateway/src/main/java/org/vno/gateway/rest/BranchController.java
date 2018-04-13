package org.vno.gateway.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private final RepoController repoController;

    @Autowired
    public BranchController(NeoBridge neoBridge,
                            MongoBridge mongoBridge,
                            RepoController repoController) {
        this.neoBridge = neoBridge;
        this.mongoBridge = mongoBridge;
        this.repoController = repoController;
        assert null != neoBridge;
        assert null != mongoBridge;
        assert null != repoController;
    }

    @GetMapping("/{repoId}/{branchId}")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long branchId) {
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(neoBridge.getBranchById(branchId));
    }

    @PutMapping("/{repoId}/{headId}/")
    ResponseEntity<?> add(@PathVariable Long repoId, @PathVariable Long headId,
                          @RequestBody Branch branch) {
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (null != neoBridge.getBranchByNameWithIdIn(branch.getName(),
                mongoBridge.getRepoById(repoId).getBranchIds())) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        branch.setId(null);
        branch.setHead(null);
        neoBridge.saveBranch(branch);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{repoId}/{branchId}")
    ResponseEntity<?> del(@PathVariable Long repoId,
                          @PathVariable Long branchId) {
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        neoBridge.deleteBranch(branchId);
        return new ResponseEntity(HttpStatus.OK);
    }

    public boolean hasAccessTo(Long branchId) {
        return repoController
                .hasAccessTo(mongoBridge.getRepoByBranch(branchId).getId());
    }
}
