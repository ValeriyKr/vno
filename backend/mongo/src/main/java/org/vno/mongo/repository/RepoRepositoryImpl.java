package org.vno.mongo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.vno.mongo.domain.Repo;

/**
 * @author kk
 */
public class RepoRepositoryImpl implements RepoRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RepoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        assert null != mongoTemplate;
    }

    @Override
    public Repo findWithMaxId() {
        final Query query = new Query()
                .limit(1)
                .with(new Sort(Sort.Direction.DESC, "id"));
        return mongoTemplate.findOne(query, Repo.class);
    }

}
