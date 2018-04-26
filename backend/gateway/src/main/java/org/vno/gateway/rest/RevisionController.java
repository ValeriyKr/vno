package org.vno.gateway.rest;

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
import org.vno.gateway.bridge.CassandraBridge;
import org.vno.gateway.bridge.MongoBridge;
import org.vno.gateway.bridge.NeoBridge;
import org.vno.gateway.domain.Blob;
import org.vno.gateway.domain.Commit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/r")
@RestController
@PreAuthorize("isAuthenticated()")
public class RevisionController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final NeoBridge neoBridge;
    private final MongoBridge mongoBridge;
    private final CassandraBridge cassandraBridge;
    private final BranchController branchController;

    @Autowired
    public RevisionController(NeoBridge neoBridge,
                              MongoBridge mongoBridge,
                              CassandraBridge cassandraBridge,
                              BranchController branchController) {
        this.neoBridge = neoBridge;
        this.mongoBridge = mongoBridge;
        this.cassandraBridge = cassandraBridge;
        this.branchController = branchController;
        assert null != neoBridge;
        assert null != mongoBridge;
        assert null != cassandraBridge;
        assert null != branchController;
    }

    /**
     * Data transferring object for commit
     */
    public static class CommitDto {
        private Commit commit;
        private ArrayList<Blob> blobs;

        public CommitDto() {}

        public Commit getCommit() {
            return commit;
        }

        public void setCommit(Commit commit) {
            this.commit = commit;
        }

        public List<Blob> getBlobs() {
            return blobs;
        }

        public void setBlobs(ArrayList<Blob> blobs) {
            this.blobs = blobs;
        }
    }

    /**
     * Retrieves commit and blobs from branch in repository
     *
     * @param repoId repository id (must be accessible for user)
     * @param branchId branch id in repository
     * @param revision revision number
     * @return commit with blobs (CommitDto), 403 or 404
     */
    @GetMapping("/full/{repoId}/{branchId}/{revision}/")
    ResponseEntity<?> getFull(@PathVariable Long repoId,
                              @PathVariable Long branchId,
                              @PathVariable Long revision) {
        if (! mongoBridge.getRepoById(repoId).getBranchIds()
                .contains(branchId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (! branchController.hasAccessTo(branchId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        CommitDto rc = new CommitDto();
        rc.setCommit(neoBridge.getCommitFromBranch(branchId, revision));
        if (null == rc.getCommit()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        rc.getCommit().setBlobIds(null == rc.getCommit().getBlobIds() ?
                new HashSet<>() : rc.getCommit().getBlobIds());
        rc.setBlobs(new ArrayList<>());
        List<Blob> blobs = mongoBridge.getBlobsByIds(
                rc.getCommit().getBlobIds());
        if (null != blobs) {
            rc.getBlobs().addAll(blobs);
        }
        return ResponseEntity.ok(rc);
    }

    /**
     * Retrieves commit from branch in repository
     *
     * @param repoId repository id (must be accessible for user)
     * @param branchId branch id in repository
     * @param revision revision number
     * @return commit without blobs (CommitDto), 403 or 404
     */
    @GetMapping("/{repoId}/{branchId}/{revision}/")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long branchId,
                          @PathVariable Long revision) {
        if (! mongoBridge.getRepoById(repoId).getBranchIds()
                .contains(branchId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (! branchController.hasAccessTo(branchId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(
                neoBridge.getCommitFromBranch(branchId, revision));
    }

    /**
     * Retrieves commits using cassandra
     *
     * @param repoId repository id (must be accessible for user)
     * @param branchId branch id in repository
     * @return list of commits
     */
    @GetMapping("/slice/{repoId}/{branchId}/")
    ResponseEntity<?> getSlice(@PathVariable Long repoId,
                               @PathVariable Long branchId) {
        if (! mongoBridge.getRepoById(repoId).getBranchIds()
                .contains(branchId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (! branchController.hasAccessTo(branchId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        List<Commit> commits = neoBridge.getCommitsByIds(
                cassandraBridge.branchSlice(repoId, branchId));
        commits.sort((o1, o2) -> o1.getRevision() < o2.getRevision() ? -1
                : (o1.getRevision() > o2.getRevision() ? +1 : 0));
        return new ResponseEntity<>(commits, HttpStatus.OK);
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
     *     "parentIds":[1, 4, 3]
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
        if (! mongoBridge.getRepoById(repoId).getBranchIds()
                .contains(branchId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (! branchController.hasAccessTo(branchId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (null == commitDto.commit || null == commitDto.blobs) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Commit commit = commitDto.getCommit();
        Long bHead = neoBridge.getBranchHead(branchId);
        if (bHead == null) {
            return new ResponseEntity<>("Branch head not found",
                    HttpStatus.NOT_FOUND);
        }
        logger.info("Commit: " + commit);
        if (null == commit.getParentIds()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (! commit.getParentIds().contains(bHead)) {
            logger.info("Conflict");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        for (Long r : commit.getParentIds()) {
            // TODO: check parents in repo
        }
        commit.setRevision(null);
        commit.setAuthorId(mongoBridge.getUserByUsername(SecurityContextHolder
                .getContext().getAuthentication().getName()).getId());
        ArrayList<Blob> blobs = commitDto.blobs;
        commit.setBlobIds(new HashSet<>());
        for (Blob blob : blobs) {
            commit.getBlobIds().add(mongoBridge.addBlob(blob).getId());
        }
        commit = neoBridge.saveCommit(commit);
        neoBridge.moveBranch(branchId, commit.getRevision());
        cassandraBridge.addRevision(commit.getAuthorId(), repoId, branchId,
                commit.getRevision());
        return ResponseEntity.ok(commit);
    }

}
