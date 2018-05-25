package org.vno.pg.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.Commit;

import java.util.Collection;

/**
 * @author kk
 */
public interface CommitRepository extends CrudRepository<Commit, Long> {
    Collection<Commit> findAll(Iterable<Long> ids);
}
