package org.vno.neo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.neo.repository.CommitRepository;

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
        return ResponseEntity.ok(commitRepository.findByRevision(revision));
    }

    @GetMapping("/all/")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(commitRepository.findAll(1));
    }

}
