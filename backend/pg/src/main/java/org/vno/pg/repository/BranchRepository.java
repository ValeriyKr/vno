package org.vno.pg.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.Branch;
import org.vno.pg.domain.Commit;

/**
 * @author kk
 */
public interface BranchRepository extends CrudRepository<Branch, Long> {
}
