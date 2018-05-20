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
import org.vno.mongo.domain.Blob;
import org.vno.mongo.repository.BlobRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@RequestMapping(value = "/blob", produces = "application/json")
@RestController
public class BlobController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    final private BlobRepository blobRepository;

    @Autowired
    public BlobController(BlobRepository blobRepository) {
        this.blobRepository = blobRepository;
        assert null != blobRepository;
    }

    @GetMapping("/get/{id}")
    ResponseEntity<?> get(@PathVariable Long id) {
        Blob blob = blobRepository.findById(id);
        if (null == blob) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
        }
        return ResponseEntity.ok(blob);
    }

    @PostMapping(value = "/add")
    ResponseEntity<?> add(@RequestBody Blob blob) {
        // TODO: check blob already exists and return it
	try {
		blob.setId(max().getId() + 1);
	} catch (NullPointerException e) {
		blob.setId(1L);
	}
        blob.setObjectId(null);
        return ResponseEntity.ok(blobRepository.save(blob));
    }

    @PostMapping(value = "/batch")
    List<Blob> batch(@RequestBody List<Long> ids) {
        return blobRepository.findByIdIn(ids);
    }

    @GetMapping("/max")
    Blob max() {
        return blobRepository.findWithMaxId();
    }

}
