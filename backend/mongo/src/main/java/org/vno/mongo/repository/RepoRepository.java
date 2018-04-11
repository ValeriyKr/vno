package org.vno.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vno.mongo.domain.Repo;

/**
 * @author kk
 */
public interface RepoRepository extends
        MongoRepository<Repo, Long>, RepoRepositoryCustom {
    Repo findById(Long id);

    Repo findByName(String name);

}
