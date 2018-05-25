package org.vno.gateway.vault;


import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;

@Table("cacheentry")
public class CacheEntryEntity {


    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
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
