package org.vno.pg.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.vno.pg.domain.Branch;
import org.vno.pg.domain.Repo;

/**
 * @author kk
 */
public interface RepoRepository extends CrudRepository<Repo, Long> {
    @Query("SELECT r FROM Repo r LEFT JOIN FETCH r.branches WHERE r.id = :id")
    Repo findOneWithBranches(@Param("id") Long id);

    Repo findByBranchesContains(Branch b);
}
