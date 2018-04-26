package org.vno.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vno.mongo.domain.UserAccount;

import java.util.Set;

/**
 * @author kk
 */
public interface UserAccountRepository extends
        MongoRepository<UserAccount, Long>, UserAccountRepositoryCustom {
    UserAccount findByUsername(String username);
    UserAccount findByUsernameOrEmail(String username, String email);
    UserAccount findById(Long id);
    Set<UserAccount> findByRepoIdsContains(Long repoId);
}
