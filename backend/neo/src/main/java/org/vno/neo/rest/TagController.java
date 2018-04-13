package org.vno.neo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.neo.domain.Tag;
import org.vno.neo.repository.CommitRepository;
import org.vno.neo.repository.TagRepository;

import java.util.Set;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/tag")
@RestController
public class TagController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final TagRepository tagRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public TagController(TagRepository tagRepository,
                         CommitRepository commitRepository) {
        this.tagRepository = tagRepository;
        this.commitRepository = commitRepository;
        assert null != tagRepository;
        assert null != commitRepository;
    }

    @GetMapping("/get/{tag}")
    ResponseEntity<?> get(@PathVariable Long tag) {
        Tag b = tagRepository.findByTag(tag);
        if (null == b) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(b);
    }

    @GetMapping("/all/")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(tagRepository.findAll(0));
    }

    static class TagAddDto {
        private Tag tag;
        private Long revision;

        public Tag getTag() {
            return tag;
        }

        public void setTag(Tag tag) {
            this.tag = tag;
        }

        public Long getRevision() {
            return revision;
        }

        public void setRevision(Long revision) {
            this.revision = revision;
        }
    }

    @PostMapping("/add")
    ResponseEntity<?> add(@RequestBody TagAddDto dto) {
        Tag b = dto.getTag();
        b.setTag(tagRepository.findMaxId() + 1);
        b.setCommit(commitRepository.findByRevision(dto.getRevision()));
        if (null == b.getCommit()) {
            return new ResponseEntity<>("No such revision: " +
                    dto.getRevision(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tagRepository.save(b));
    }

    @DeleteMapping("/del/{tag}")
    void del(@PathVariable Long tag) {
        tagRepository.delete(tagRepository.findByTag(tag));
    }

    @PostMapping("/get/by_name_with_id_in/{name}")
    Tag getByNameWithIdIn(@PathVariable String name,
                          @RequestBody Set<Long> ids) {
        Set<Tag> bb = tagRepository.findByNameAndTagIn(name, ids);
        if (null == bb || bb.isEmpty()) {
            return null;
        }
        return bb.stream().findAny().orElse(null);
    }

}
