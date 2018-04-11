package org.vno.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.vno.mongo.domain.Role;

/**
 * @author kk
 */
public interface RoleRepository extends
        MongoRepository<Role, Long> {
    Role findById(Long id);
    Role findByName(String name);
}
