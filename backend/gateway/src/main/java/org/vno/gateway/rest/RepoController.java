package org.vno.gateway.rest;

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
import org.vno.gateway.bridge.MongoBridge;
import org.vno.gateway.domain.Repo;
import org.vno.gateway.domain.UserAccount;

import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/repo")
@RestController
@PreAuthorize("isAuthenticated()")
public class RepoController {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final MongoBridge mongoBridge;

    @Autowired
    public RepoController(MongoBridge mongoBridge) {
        this.mongoBridge = mongoBridge;
        assert null != mongoBridge;
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
        return ResponseEntity.ok(mongoBridge.getRepoById(id));
    }

    /**
     * Creates new repository
     *
     * @param repo repository description object
     * @return 200 ok, or bad request (already exists)
     */
    @PutMapping("/")
    ResponseEntity<?> add(@RequestBody Repo repo) {
        UserAccount owner = mongoBridge.getUserByUsername(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName());
        if (null == owner) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        return mongoBridge.addRepo(repo, owner);
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
        UserAccount user = mongoBridge.getUserByUsername(authentication
                .getName());
        return user.repoIds.contains(id);
    }

}
