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
import org.vno.gateway.bridge.MongoBridge;
import org.vno.gateway.bridge.NeoBridge;
import org.vno.gateway.domain.Tag;

/**
 * @author kk
 */
@RequestMapping("/tag")
@RestController
@PreAuthorize("isAuthenticated()")
public class TagController {

    private final NeoBridge neoBridge;
    private final MongoBridge mongoBridge;
    private final RepoController repoController;

    @Autowired
    public TagController(NeoBridge neoBridge,
                         MongoBridge mongoBridge,
                         RepoController repoController) {
        this.neoBridge = neoBridge;
        this.mongoBridge = mongoBridge;
        this.repoController = repoController;
        assert null != neoBridge;
        assert null != mongoBridge;
        assert null != repoController;
    }

    /**
     * Returns tag by its and repo's ids
     * TODO: why doesn't this method have a check for relation between tag and
     * TODO: repo?
     *
     * @param repoId repository id, where tag must be presented
     * @param tagId id of required tag
     * @return tag or 403 (TODO: 404)
     */
    @GetMapping("/{repoId}/{tagId}")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long tagId) {
        if (! repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(neoBridge.getTagById(tagId));
    }

    /**
     * Creates new tag
     *
     * @param repoId repository to store tag
     * @param headId commit to point by tag
     * @param name tag name
     * @return 200 on success, 403 if don't have permissions, 400 if already
     * exists
     */
    @PutMapping("/{repoId}/{headId}/{name}")
    ResponseEntity<?> add(@PathVariable Long repoId, @PathVariable Long headId,
                          @PathVariable String name) {
        Tag tag = new Tag();
        tag.setName(name);
        if (! repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (null != neoBridge.getTagByNameWithIdIn(tag.getName(),
                mongoBridge.getRepoById(repoId).getTagIds())) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        tag.setId(null);
        tag.setHead(null); // TODO: bug
        neoBridge.saveTag(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Deletes a tag
     *
     * @param repoId id of repository, which is storing tag
     * @param tagId id of tag
     * @return 403 if not permitted, 200 if deleted
     */
    @DeleteMapping("/{repoId}/{tagId}")
    ResponseEntity<?> del(@PathVariable Long repoId,
                          @PathVariable Long tagId) {
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        neoBridge.deleteTag(tagId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
