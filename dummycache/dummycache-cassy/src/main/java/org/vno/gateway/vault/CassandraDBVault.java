package org.vno.gateway.vault;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

public class CassandraDBVault implements CacheVault {

    private CacheEntryRepository vault;

    public CassandraDBVault(CacheEntryRepository cerVault) {
        assert cerVault != null;
        this.vault = cerVault;
    }

    private boolean isInvalidEntry(CacheEntryEntity entity) {
        long differenceInSeconds = (new Date().getTime() - entity.getLastRequestedDate().getTime()) / 1000;
        return (entity.getRequestedCount() > 10) || (differenceInSeconds > 30);
    }

    private void justRequested(CacheEntryEntity entity) {
        entity.setRequestedCount(entity.getRequestedCount() + 1);
        entity.setLastRequestedDate(new Date());
    }

    public String get(String key) {
        if (vault == null) {
            System.out.println("fuck");
        }
        CacheEntryEntity entry = vault.findByKey(key);

        if (entry == null) return null;

        if (isInvalidEntry(entry)) {
            System.out.println("Found entry in cache vault, but entry is invalid.");
            vault.delete(entry);
            return null;
        }

        justRequested(entry);
        vault.save(entry);
        return entry.getValue();
    }
    public void put(String key, String value) {
        vault.save(new CacheEntryEntity(key, value));
    }
}
