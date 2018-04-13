package org.vno.neo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.neo.domain.Commit;
import org.vno.neo.repository.CommitRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/r")
@RestController
public class RevisionController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final CommitRepository commitRepository;

    @Autowired
    public RevisionController(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
        assert null != commitRepository;
    }

    @GetMapping("/get/{revision}")
    ResponseEntity<?> get(@PathVariable Long revision) {
        Optional<Commit> commitOptional = commitRepository
                .findByRevisionWithDependency(revision).stream()
                .filter(c -> c.getRevision().equals(revision)).findAny();
        if (! commitOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Commit commit = commitOptional.get();
        return ResponseEntity.ok(commit);
    }

    @GetMapping("/all/")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(commitRepository.findAll(1));
    }

    @PutMapping("/")
    Commit add(@RequestBody Commit commit) {
        Set<Long> parents =  commit.getDtoParentIds();
        commit.setParents(new HashSet<>());
        for (Long p : parents) {
            commit.getParents().add(commitRepository.findByRevision(p));
        }
        Long maxRevision = commitRepository.findMaxId();
        commit.setRevision(maxRevision == null ? 0 : maxRevision + 1);
        commit = commitRepository.save(commit);
        logger.info("Commit saved: " + commit);
        return commit;
    }

    @GetMapping("/{branch}/{revision}")
    Commit getCommitFromBranch(@PathVariable Long branch,
                               @PathVariable Long revision) {
        return commitRepository.findFromBranch(branch, revision);
    }

}
