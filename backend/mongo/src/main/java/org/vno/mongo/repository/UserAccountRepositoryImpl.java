package org.vno.mongo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.vno.mongo.domain.UserAccount;

/**
 * @author kk
 */
public class UserAccountRepositoryImpl implements UserAccountRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserAccountRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        assert null != mongoTemplate;
    }

    @Override
    public UserAccount findWithMaxId() {
        final Query query = new Query()
                .limit(1)
                .with(new Sort(Sort.Direction.DESC, "id"));
        return mongoTemplate.findOne(query, UserAccount.class);
    }

}
