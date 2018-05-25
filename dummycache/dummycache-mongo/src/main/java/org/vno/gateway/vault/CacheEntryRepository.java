package org.vno.gateway.vault;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CacheEntryRepository extends MongoRepository<CacheEntryEntity, String> {
    public CacheEntryEntity findByKey(String key);
    public void deleteByKey(String key);
}
