package org.vno.pg.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.Blob;

/**
 * @author kk
 */
public interface BlobRepository extends CrudRepository<Blob, Long> {
}
