package org.vno.cassandra.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.cassandra.db.BranchRepository;

import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping(value = "/ref", produces = "application/json")
@RestController
public class BranchController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final BranchRepository branchRepository;

    @Autowired
    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
        assert null != branchRepository;
    }

    @PutMapping("/{repoId}/{branchId}")
    void add(@PathVariable Long repoId,
             @PathVariable Long branchId) {
        branchRepository.add(repoId, branchId);
    }

}
