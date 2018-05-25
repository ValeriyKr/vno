package org.vno.gateway.vault;

public interface CacheVault {
    public String get(String key);
    public void put(String key, String value);
}
