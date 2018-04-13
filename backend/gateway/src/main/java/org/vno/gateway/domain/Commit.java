package org.vno.gateway.domain;

import java.util.Set;

/**
 * @author kk
 */
public class Commit {
    Long id;
    private Long revision;
    private Long authorId;
    private String message;
    private Long timestamp;
    private Long rootId;
    private Set<Long> parentIds;
    private Set<Long> blobIds;

    Commit() {}

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Set<Long> getParentIds() {
        return parentIds;
    }

    public void setParentIds(Set<Long> parentIds) {
        this.parentIds = parentIds;
    }

    public Set<Long> getBlobIds() {
        return blobIds;
    }

    public void setBlobIds(Set<Long> blobIds) {
        this.blobIds = blobIds;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "revision=" + (revision == null ? "null" : revision) +
                ", authorId=" + (authorId == null ? "null" : authorId) +
                ", timestamp=" + (timestamp == null ? "null" : timestamp) +
                ", rootId=" + (rootId == null ? "null" : rootId) +
                ", parentId=" + (getParentIds() == null ? "null" :
                getParentIds()) +
                ", blobIds=" + (blobIds == null ? "null" : blobIds) +
                '}';
    }
}
