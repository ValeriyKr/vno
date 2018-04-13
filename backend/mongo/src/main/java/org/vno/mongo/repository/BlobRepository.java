package org.vno.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vno.mongo.domain.Blob;

import java.util.List;

/**
 * @author kk
 */
public interface BlobRepository extends
        MongoRepository<Blob, Long>, BlobRepositoryCustom {
    Blob findById(Long id);

    Blob findByName(String name);

    List<Blob> findByIdIn(List<Long> ids);

}
