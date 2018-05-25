package org.vno.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.domain.Commit;

import java.util.Collection;

/**
 * @author kk
 */
public interface CommitRepository extends CrudRepository<Commit, Long> {
    Collection<Commit> findAll(Iterable<Long> ids);
}
