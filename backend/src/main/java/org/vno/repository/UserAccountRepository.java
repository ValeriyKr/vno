package org.vno.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.vno.domain.Repo;
import org.vno.domain.UserAccount;

/**
 * @author kk
 */
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @EntityGraph(attributePaths = {"roles"},
            type = EntityGraph.EntityGraphType.LOAD)
    UserAccount findByUsername(String username);

    @Query("FROM UserAccount u LEFT JOIN FETCH u.repos WHERE u.id = :id")
    UserAccount findOneWithRepos(@Param("id") Long id);

    UserAccount findByUsernameOrEmail(String username, String email);

    @Query("SELECT COUNT(u) FROM UserAccount u " +
            "WHERE :repoId IN (SELECT id FROM u.repos) AND u.username = :username")
    Long hasAccessTo(@Param("username") String username,
                     @Param("repoId") Long repoId);
}
