package org.vno.pg.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.Commit;

/**
 * @author kk
 */
public interface CommitRepository extends CrudRepository<Commit, Long> {
}
