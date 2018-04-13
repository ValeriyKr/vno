package org.vno.gateway.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.gateway.bridge.MongoBridge;
import org.vno.gateway.domain.UserAccount;

import java.util.Collections;
import java.util.HashSet;

/**
 * @author kk
 */
@RequestMapping(value = "/user", produces = "application/json")
@RestController
@PreAuthorize("isAuthenticated()")
public class UserAccountController {

    private final MongoBridge mongoBridge;

    @Autowired
    public UserAccountController(MongoBridge mongoBridge) {
        this.mongoBridge = mongoBridge;
        assert null != mongoBridge;
    }

    /**
     * @param id required user id
     * @return user info or 404 not found
     */
    @GetMapping("/{id}")
    ResponseEntity<?> get(@PathVariable Long id) {
        UserAccount user = mongoBridge.getUserById(id);
        if (null == user) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            user.setPassword("");
            return ResponseEntity.ok(user);
        }
    }

    /**
     * @return signed-in user info
     */
    @GetMapping("/me/")
    ResponseEntity<?> get() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserAccount user = mongoBridge.getUserByUsername(authentication
                .getName());
        user.setPassword("");
        return ResponseEntity.ok(user);
    }

    /**
     * Registers new user
     * TODO: permit all or secure to admin level
     *
     * @param user user info, including password
     * @return 400 if username or email are presented, 200 otherwise
     */
    @PostMapping("/register/")
    ResponseEntity<?> register(@RequestBody UserAccount user) {
        if (! HttpStatus.NOT_FOUND.equals(mongoBridge.testUsernameOrEmail(
                user.getUsername(), user.getEmail()))) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        user.setRoleIds(new HashSet<>(Collections.singleton(
                mongoBridge.getRoleByName("user").getId())));
        return mongoBridge.add(user);
    }

}
