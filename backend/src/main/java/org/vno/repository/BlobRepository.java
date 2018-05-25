package org.vno.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.domain.Blob;

/**
 * @author kk
 */
public interface BlobRepository extends CrudRepository<Blob, Long> {
}
