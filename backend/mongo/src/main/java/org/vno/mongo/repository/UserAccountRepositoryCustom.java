package org.vno.mongo.repository;

import org.vno.mongo.domain.UserAccount;

/**
 * @author kk
 */
public interface UserAccountRepositoryCustom {
    UserAccount findWithMaxId();
}
