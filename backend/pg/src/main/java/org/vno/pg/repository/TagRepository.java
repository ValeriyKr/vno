package org.vno.pg.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.Tag;

/**
 * @author kk
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
}
