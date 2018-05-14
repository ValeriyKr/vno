package org.vno.pg.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kk
 */
@RequestMapping("/echo")
@RestController
public class Echo {

    @GetMapping("/{what}")
    ResponseEntity<?> what(@PathVariable String what) {
        return ResponseEntity.ok(what);
    }

}
