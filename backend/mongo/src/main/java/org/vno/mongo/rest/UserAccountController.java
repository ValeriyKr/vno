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
import org.vno.mongo.domain.UserAccount;
import org.vno.mongo.repository.UserAccountRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping(value = "/user", produces = "application/json")
@RestController
public class UserAccountController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    final private UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
        assert null != userAccountRepository;
    }

    @GetMapping("/get/{username}")
    ResponseEntity<?> get(@PathVariable String username) {
        UserAccount user = userAccountRepository.findByUsername(username);
        if (null == user) {
            logger.info("No user found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            logger.info(user.toString());
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get/{username}/{email}")
    ResponseEntity<?> get(@PathVariable String username,
                             @PathVariable String email) {
        logger.info("username: " + username);
        logger.info("email: " + email);
        UserAccount user = userAccountRepository.findByUsernameOrEmail(username,
                email);
        if (null == user) {
            logger.info("No user found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            logger.info(user.toString());
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all/")
    ResponseEntity<?> all() {
        List<UserAccount> users = userAccountRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    ResponseEntity<?> add(@RequestBody UserAccount user) {
        logger.info("add");
        if (null != userAccountRepository.findByUsernameOrEmail(
                user.getUsername(), user.getEmail())) {
            logger.info("Already exists");
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
        logger.info("saving");
        user.setId(userAccountRepository.findWithMaxId().getId() + 1);
        user = userAccountRepository.save(user);
        logger.info("saved: " + user.toString());
        return ResponseEntity.ok(user);
    }

}
