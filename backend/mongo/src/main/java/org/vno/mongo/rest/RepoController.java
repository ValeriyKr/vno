package org.vno.mongo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.mongo.domain.Repo;
import org.vno.mongo.domain.UserAccount;
import org.vno.mongo.repository.RepoRepository;
import org.vno.mongo.repository.UserAccountRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author kk
 */
@RequestMapping(value = "/repo", produces = "application/json")
@RestController
public class RepoController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    final private RepoRepository repoRepository;
    final private UserAccountRepository userAccountRepository;

    @Autowired
    public RepoController(RepoRepository repoRepository,
                          UserAccountRepository userAccountRepository) {
        this.repoRepository = repoRepository;
        this.userAccountRepository = userAccountRepository;
        assert null != repoRepository;
        assert null != userAccountRepository;
    }

    @GetMapping("/get/{id}")
    ResponseEntity<?> get(@PathVariable Long id) {
        Repo repo = repoRepository.findById(id);
        if (null == repo) {
            logger.info("No repos found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            logger.info(repo.toString());
        }
        return ResponseEntity.ok(repo);
    }

    static class AddRequestWrapper {
        Repo repo;
        UserAccount user;

        AddRequestWrapper() {}

        public Repo getRepo() {
            return repo;
        }

        public void setRepo(Repo repo) {
            this.repo = repo;
        }

        public UserAccount getUser() {
            return user;
        }

        public void setUser(UserAccount user) {
            this.user = user;
        }
    }

    @PostMapping(value = "/add")
    ResponseEntity<?> add(@RequestBody AddRequestWrapper request) {
        UserAccount user = request.getUser();
        Repo repo = request.getRepo();

        UserAccount owner = userAccountRepository.findByUsername(
                user.getUsername());
        if (null == owner) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Repo existing = repoRepository.findByName(repo.getName());
        if (null != existing) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        repo.setId(repoRepository.findWithMaxId().getId() + 1);
        repo = repoRepository.save(repo);
        if (owner.getRepoIds() == null) {
            owner.setRepoIds(new HashSet<>(Collections
                    .singleton(repo.getId())));
        } else {
            owner.getRepoIds().add(repo.getId());
        }
        userAccountRepository.save(user);
        return ResponseEntity.ok(repo);
    }

    @GetMapping("/max")
    Repo max() {
        return repoRepository.findWithMaxId();
    }

    @GetMapping("/collaborators/{repoId}")
    List<Long> collaborators(@PathVariable Long repoId) {
        return userAccountRepository.findByRepoIdsContains(repoId).stream()
                .mapToLong(UserAccount::getId)
                .boxed().collect(Collectors.toList());
    }

    @GetMapping("/get_by_branch/{id}")
    Repo getByBranch(@PathVariable Long id) {
        List<Repo> r = repoRepository.findByBranchIdsContains(id);
        if (null == r || r.isEmpty()) {
            return null;
        }
        return r.get(0);
    }

}
