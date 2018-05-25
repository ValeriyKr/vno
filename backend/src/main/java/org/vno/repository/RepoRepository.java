package org.vno.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.vno.domain.Branch;
import org.vno.domain.Repo;
import org.vno.domain.Tag;

/**
 * @author kk
 */
public interface RepoRepository extends CrudRepository<Repo, Long> {
    @Query("SELECT r FROM Repo r LEFT JOIN FETCH r.branches WHERE r.id = :id")
    Repo findOneWithBranches(@Param("id") Long id);

    Repo findByBranchesContains(Branch b);

    @Query("SELECT r FROM Repo r LEFT JOIN FETCH r.tags WHERE r.id = :id")
    Repo findOneWithTags(@Param("id") Long id);

    Repo findByTagsContains(Tag t);
}
