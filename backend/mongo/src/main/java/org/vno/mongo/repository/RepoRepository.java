package org.vno.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vno.mongo.domain.Repo;

import java.util.List;

/**
 * @author kk
 */
public interface RepoRepository extends
        MongoRepository<Repo, Long>, RepoRepositoryCustom {
    Repo findById(Long id);

    Repo findByName(String name);

    List<Repo> findByBranchIdsContains(Long branchId);

}
