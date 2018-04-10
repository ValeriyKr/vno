package org.vno.gateway.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kk
 */
@RequestMapping("/echo")
@RestController
@PreAuthorize("isAuthenticated()")
public class Echo {

    @GetMapping("/{what}")
    ResponseEntity<?> what(@PathVariable String what) {
        return ResponseEntity.ok(what);
    }

}
