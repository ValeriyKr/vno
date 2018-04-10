package org.vno.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.vno.mongo.domain.UserAccount;

/**
 * @author kk
 */
public interface UserAccountRepository extends
        MongoRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);
}
