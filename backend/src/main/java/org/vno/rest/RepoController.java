package org.vno.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.domain.Repo;
import org.vno.domain.UserAccount;
import org.vno.repository.RepoRepository;
import org.vno.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/repo")
@RestController
@PreAuthorize("isAuthenticated()")
public class RepoController {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final RepoRepository repoRepository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public RepoController(RepoRepository repoRepository,
                          UserAccountRepository userAccountRepository) {
        this.repoRepository = repoRepository;
        this.userAccountRepository = userAccountRepository;
    }


    /**
     * Retrieves repository
     *
     * @param id repository id (must be accessible)
     * @return repository description object
     */
    @GetMapping("/{id}")
    ResponseEntity<?> get(@PathVariable Long id) {
        if (! hasAccessTo(id)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(repoRepository.findOne(id));
    }

    /**
     * Creates new repository
     *
     * @param repo repository description object
     * @return 200 ok, or bad request (already exists)
     */
    @PutMapping("/")
    ResponseEntity<?> add(@RequestBody Repo repo) {
        UserAccount owner = userAccountRepository.findByUsername(
                SecurityContextHolder
                .getContext().getAuthentication().getName());
        repo.setId(null);
        repo.setOwners(new HashSet<>(Collections.singleton(owner)));
        repo = repoRepository.save(repo);
        owner.setRepos(new HashSet<>(Collections.singleton(repo)));
        userAccountRepository.save(owner);

        return new ResponseEntity<>(repo, HttpStatus.OK);
    }

    /**
     * Checks repository access for current user
     *
     * @param id repository id
     * @return true if user has access to repo with given id
     */
    public boolean hasAccessTo(Long id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return 1 == userAccountRepository.hasAccessTo(
                authentication.getName(), id);
    }

}
