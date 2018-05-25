package org.vno.gateway.vault;

import java.util.Date;
import java.util.HashMap;

class CacheEntry {
    private String value;
    private Date lastRequestedDate;
    private int requestedCount;

    public CacheEntry(String value) {
        this.value = value;
        this.lastRequestedDate = new Date();
        this.requestedCount = 0;
    }

    public String getValue() {
        return value;
    }

    public void justRequested() {
        this.requestedCount++;
        this.lastRequestedDate = new Date();
    }

    public boolean invalidEntry() {
        long differenceInSeconds = (new Date().getTime() - this.lastRequestedDate.getTime()) / 1000;
        return (requestedCount > 5) || (differenceInSeconds > 10);
    }
}

public class HashMapVault implements CacheVault {

    private HashMap<String, CacheEntry> vault;

    public HashMapVault() {
        vault = new HashMap<>();
    }

    public String get(String key) {
        CacheEntry entry = vault.get(key);

        if (entry == null) return null;

        if (entry.invalidEntry()) {
            System.out.println("Found entry in cache vault, but entry is invalid.");
            vault.remove(key);
            return null;
        }

        entry.justRequested();
        return entry.getValue();
    }
    public void put(String key, String value) {
        vault.put(key, new CacheEntry(value));
    }
}
