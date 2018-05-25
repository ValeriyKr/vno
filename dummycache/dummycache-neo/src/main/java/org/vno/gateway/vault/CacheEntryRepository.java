package org.vno.gateway.vault;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CacheEntryRepository extends Neo4jRepository<CacheEntryEntity, Long> {
    public CacheEntryEntity findByKey(String key);
    public void deleteByKey(String key);
}
