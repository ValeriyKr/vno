package org.vno.neo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.neo.domain.Branch;
import org.vno.neo.domain.Commit;
import org.vno.neo.repository.BranchRepository;
import org.vno.neo.repository.CommitRepository;

import java.util.Set;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/ref")
@RestController
public class BranchController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private final BranchRepository branchRepository;
    private final CommitRepository commitRepository;

    @Autowired
    public BranchController(BranchRepository branchRepository,
                            CommitRepository commitRepository) {
        this.branchRepository = branchRepository;
        this.commitRepository = commitRepository;
        assert null != branchRepository;
        assert null != commitRepository;
    }

    @GetMapping("/get/{branch}")
    ResponseEntity<?> get(@PathVariable Long branch) {
        Branch b = branchRepository.findByBranch(branch);
        if (null == b) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(b);
    }

    @GetMapping("/head/{branch}")
    Long head(@PathVariable Long branch) {
        Commit h = branchRepository.findHead(branch);
        if (null == h) {
            logger.info("head not found");
            return null;
        }
        logger.info("head: " + h);
        return h.getRevision();
    }

    @GetMapping("/all/")
    ResponseEntity<?> all() {
        return ResponseEntity.ok(branchRepository.findAll(0));
    }

    static class BranchAddDto {
        private Branch branch;
        private Long revision;

        public Branch getBranch() {
            return branch;
        }

        public void setBranch(Branch branch) {
            this.branch = branch;
        }

        public Long getRevision() {
            return revision;
        }

        public void setRevision(Long revision) {
            this.revision = revision;
        }
    }

    @PutMapping("/add/")
    ResponseEntity<?> add(@RequestBody BranchAddDto dto) {
        Branch b = dto.getBranch();
        Long maxRevision = branchRepository.findMaxId();
        b.setBranch(maxRevision == null ? 0 : maxRevision + 1);
        b.setCommit(commitRepository.findByRevision(dto.getRevision()));
        if (null == b.getCommit()) {
            return new ResponseEntity<>("No such revision: " +
                    dto.getRevision(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(branchRepository.save(b));
    }

    @DeleteMapping("/del/{branch}")
    void del(@PathVariable Long branch) {
        branchRepository.delete(branchRepository.findByBranch(branch));
    }

    @PostMapping("/get/by_name_with_id_in/{name}")
    Branch getByNameWithIdIn(@PathVariable String name,
                             @RequestBody Set<Long> ids) {
        Set<Branch> bb = branchRepository.findByNameAndBranchIn(name, ids);
        if (null == bb || bb.isEmpty()) {
            return null;
        }
        return bb.stream().findAny().orElse(null);
    }

    @PostMapping("/move/{branchId}/{revision}")
    void move(@PathVariable Long branchId, @PathVariable Long revision) {
        branchRepository.updateBranchHead(branchId, revision);
    }

}
