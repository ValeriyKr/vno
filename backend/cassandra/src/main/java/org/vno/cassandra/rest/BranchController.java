package org.vno.cassandra.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.cassandra.db.BranchRepository;
import org.vno.cassandra.db.UserRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping(value = "/ref", produces = "application/json")
@RestController
public class BranchController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Autowired
    public BranchController(BranchRepository branchRepository,
                            UserRepository userRepository) {
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        assert null != branchRepository;
        assert null != userRepository;
    }

    @PutMapping("/{repoId}/{branchId}")
    void add(@PathVariable Long repoId,
             @PathVariable Long branchId,
             @RequestBody List<Long> userIds) {
        Long users[] = new Long[0];
        if (null != userIds) {
            users = userIds.toArray(new Long[userIds.size()]);
        }
        branchRepository.add(repoId, branchId, users);
        if (null != userIds) {
            for (Long userId : userIds) {
                userRepository.add(userId, repoId);
                userRepository.alter(userId, repoId, branchId);
            }
        }
    }

}
