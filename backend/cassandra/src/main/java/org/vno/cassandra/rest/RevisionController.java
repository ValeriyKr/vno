package org.vno.cassandra.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.cassandra.db.BranchRepository;
import org.vno.cassandra.db.UserRepository;

import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping(value = "/r", produces = "application/json")
@RestController
public class RevisionController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Autowired
    public RevisionController(BranchRepository branchRepository,
                              UserRepository userRepository) {
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        assert null != branchRepository;
        assert null != userRepository;
    }

    @PutMapping("/{userId}/{repoId}/{branchId}/{revision}")
    void add(@PathVariable Long userId,
             @PathVariable Long repoId,
             @PathVariable Long branchId,
             @PathVariable Long revision) {
        branchRepository.commit(userId, repoId, branchId, revision);
        userRepository.commit(userId, repoId, branchId, revision);
    }

}
