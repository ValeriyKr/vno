package org.vno.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.domain.Branch;
import org.vno.domain.Commit;

/**
 * @author kk
 */
public interface BranchRepository extends CrudRepository<Branch, Long> {
}
