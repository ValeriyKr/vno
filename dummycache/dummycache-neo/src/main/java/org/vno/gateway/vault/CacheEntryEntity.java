package org.vno.gateway.vault;


import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.annotation.Id;
import sun.misc.Cache;

import java.util.Date;

@NodeEntity(label = "cacheentity")
public class CacheEntryEntity {

    @Id
    private Long id;

    private String key;

    private String value;

    private Date lastRequestedDate;

    private int requestedCount;

    public CacheEntryEntity() {};

    public CacheEntryEntity(String key, String value, Date lastRequestedDate, int requestedCount) {
        this.key = key;
        this.value = value;
        this.lastRequestedDate = lastRequestedDate;
        this.requestedCount = requestedCount;
    }

    public CacheEntryEntity(String key, String value) {
        this(key, value, new Date(), 0);
    }

    @Override
    public String toString() {
        return "CacheEntryEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", lastRequestedDate=" + lastRequestedDate +
                ", requestedCount=" + requestedCount +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getLastRequestedDate() {
        return lastRequestedDate;
    }

    public void setLastRequestedDate(Date lastRequestedDate) {
        this.lastRequestedDate = lastRequestedDate;
    }

    public int getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(int requestedCount) {
        this.requestedCount = requestedCount;
    }
}
