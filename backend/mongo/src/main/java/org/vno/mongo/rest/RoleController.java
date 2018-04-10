package org.vno.mongo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vno.mongo.domain.Role;
import org.vno.mongo.repository.RoleRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping("/role")
@RestController
public class RoleController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    final private RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        assert null != roleRepository;
    }


    @GetMapping("/get/{id}")
    ResponseEntity<?> get(@PathVariable Long id) {
        Role role = roleRepository.findById(id);
        if (null != role) {
            logger.info(role.toString());
        } else {
            logger.info("No roles found");
        }
        return ResponseEntity.ok(role);
    }

    @GetMapping("/all/")
    ResponseEntity<?> get() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

}
