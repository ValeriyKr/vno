package org.vno.mongo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.vno.mongo.domain.Blob;

/**
 * @author kk
 */
public class BlobRepositoryImpl implements BlobRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public BlobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        assert null != mongoTemplate;
    }

    @Override
    public Blob findWithMaxId() {
        final Query query = new Query()
                .limit(1)
                .with(new Sort(Sort.Direction.DESC, "id"));
        return mongoTemplate.findOne(query, Blob.class);
    }

}
