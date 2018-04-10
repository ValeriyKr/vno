package org.vno.mongo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.mongo.domain.UserAccount;
import org.vno.mongo.repository.UserAccountRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/user")
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
        if (null != user) {
            logger.info(user.toString());
        } else {
            logger.info("No user found");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all/")
    ResponseEntity<?> get() {
        List<UserAccount> users = userAccountRepository.findAll();
        return ResponseEntity.ok(users);
    }

}
