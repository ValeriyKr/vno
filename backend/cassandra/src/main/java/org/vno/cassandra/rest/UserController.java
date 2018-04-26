package org.vno.cassandra.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.cassandra.db.BranchRepository;
import org.vno.cassandra.db.UserRepository;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping(value = "/user", produces = "application/json")
@RestController
public class UserController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          BranchRepository branchRepository) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        assert null != userRepository;
        assert null != branchRepository;
    }

    @PutMapping("/{userId}/{repoId}")
    void add(@PathVariable Long userId,
             @PathVariable Long repoId,
             @Null @RequestBody List<Long> branchIds) {
        Long branches[] = new Long[0];
        if (null != branchIds) {
            branches = branchIds.toArray(new Long[branchIds.size()]);
        }
        userRepository.add(userId, repoId, branches);
        if (null != branchIds) {
            for (Long branchId : branchIds) {
                branchRepository.add(repoId, branchId);
                branchRepository.alter(repoId, branchId, userId);
            }
        }
    }

}
