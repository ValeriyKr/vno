package org.vno.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.domain.Tag;

/**
 * @author kk
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
}
