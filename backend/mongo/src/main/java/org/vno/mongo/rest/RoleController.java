package org.vno.mongo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (null == role) {
            logger.info("No roles found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            logger.info(role.toString());
        }
        return ResponseEntity.ok(role);
    }

    @GetMapping("/get_by_name/{name}")
    ResponseEntity<?> get(@PathVariable String name) {
        Role role = roleRepository.findByName(name);
        if (null == role) {
            logger.info("No roles found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            logger.info(role.toString());
        }
        return ResponseEntity.ok(role);
    }

    @GetMapping("/all/")
    ResponseEntity<?> get() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

}
