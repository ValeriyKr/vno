package org.vno.neo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.neo.domain.Branch;
import org.vno.neo.repository.BranchRepository;

import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/ref")
@RestController
public class BranchController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final BranchRepository branchRepository;

    @Autowired
    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
        assert null != branchRepository;
    }

    @GetMapping("/get/{branch}")
    ResponseEntity<?> get(@PathVariable Long branch) {
        Branch b = branchRepository.findByBranch(branch);
        if (null == b) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(b);
    }

    @GetMapping("/all/")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(branchRepository.findAll(0));
    }

}
