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
import org.vno.gateway.domain.Tag;

/**
 * @author kk
 */
@RequestMapping("/ref")
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

    @GetMapping("/{repoId}/{tagId}")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long tagId) {
        if (! repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(neoBridge.getTagById(tagId));
    }

    @PutMapping("/{repoId}/{headId}/")
    ResponseEntity<?> add(@PathVariable Long repoId, @PathVariable Long headId,
                          @RequestBody Tag tag) {
        if (! repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (null != neoBridge.getTagByNameWithIdIn(tag.getName(),
                mongoBridge.getRepoById(repoId).getTagIds())) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        tag.setId(null);
        tag.setHead(null);
        neoBridge.saveTag(tag);
        return new ResponseEntity(HttpStatus.OK);
    }

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
