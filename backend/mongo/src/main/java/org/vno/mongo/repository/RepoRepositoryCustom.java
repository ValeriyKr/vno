package org.vno.mongo.repository;

import org.vno.mongo.domain.Repo;

/**
 * @author kk
 */
public interface RepoRepositoryCustom {
    Repo findWithMaxId();
}
