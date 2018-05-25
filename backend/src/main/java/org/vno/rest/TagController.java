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
import org.vno.domain.Tag;
import org.vno.domain.Commit;
import org.vno.domain.Repo;
import org.vno.repository.TagRepository;
import org.vno.repository.CommitRepository;
import org.vno.repository.RepoRepository;

import java.util.HashSet;

/**
 * @author kk
 */
@RequestMapping("/tag")
@RestController
@PreAuthorize("isAuthenticated()")
public class TagController {

    private final RepoController repoController;
    private final TagRepository tagRepository;
    private final RepoRepository repoRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public TagController(RepoController repoController,
                         TagRepository tagRepository,
                         RepoRepository repoRepository,
                         CommitRepository commitRepository) {
        this.repoController = repoController;
        this.tagRepository = tagRepository;
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
    }

    /**
     * Returns tag by its and repo's ids
     * TODO: why doesn't this method have a check for relation between tag
     * TODO: and repo?
     *
     * @param repoId repository id, where tag must be presented
     * @param tagId id of required tag
     * @return tag or 403 (TODO: 404)
     */
    @GetMapping("/{repoId}/{tagId}")
    ResponseEntity<?> get(@PathVariable Long repoId,
                          @PathVariable Long tagId) {
        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Tag b = tagRepository.findOne(tagId);
        if (! b.getRepo().getId().equals(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(b);
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
        Repo r = repoRepository.findOneWithTags(repoId);
        if (null == r) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (!repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (r.getTags() == null) {
            r.setTags(new HashSet<>());
        }
        if (r.getTags().stream().anyMatch(b -> b.getName().equals(name))) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }

        Commit c = commitRepository.findOne(headId);
        if (null == c) {
            return new ResponseEntity<>("No such revision",
                    HttpStatus.NOT_FOUND);
        }

        Tag tag = new Tag();
        tag.setName(name);
        tag.setTag(null);
        tag.setHead(c); // TODO: commit in repo?
        tag.setRepo(r);
        tag = tagRepository.save(tag);
        r.getTags().add(tag);
        repoRepository.save(r);

        return new ResponseEntity<>(tag, HttpStatus.OK);
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
        if (! repoController.hasAccessTo(repoId)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        tagRepository.delete(tagId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Checks tag access for current user
     *
     * @param tagId tag id
     * @return true if user has access to tag with given id
     */
    public boolean hasAccessTo(Long tagId) {
        return repoController
                .hasAccessTo(repoRepository.findByTagsContains(
                        tagRepository.findOne(tagId)).getId());
    }
}
