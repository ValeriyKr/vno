package org.vno.pg.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.vno.pg.domain.UserAccount;

/**
 * @author kk
 */
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    @EntityGraph(attributePaths = {"roles"},
            type = EntityGraph.EntityGraphType.LOAD)
    UserAccount findByUsername(String username);

    UserAccount findByUsernameOrEmail(String username, String email);
}
