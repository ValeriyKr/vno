package org.vno.pg.repository;

import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.Repo;

/**
 * @author kk
 */
public interface RepoRepository extends CrudRepository<Repo, Long> {
}
