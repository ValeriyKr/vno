package org.vno.gateway.vault;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CacheEntryRepository extends CassandraRepository<CacheEntryEntity/*, Long*/> {
    @Query("SELECT * FROM cacheentry WHERE key = ?0 ALLOW FILTERING")
    public CacheEntryEntity findByKey(String key);
}
