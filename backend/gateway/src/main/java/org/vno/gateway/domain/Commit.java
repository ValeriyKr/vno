package org.vno.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private Long parentId;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "revision=" + (revision == null ? "null" : revision) +
                ", authorId=" + (authorId == null ? "null" : authorId) +
                ", timestamp=" + (timestamp == null ? "null" : timestamp) +
                ", rootId=" + (rootId == null ? "null" : rootId) +
                ", parentId=" + (getParentId() == null ? "null" :
                getParentId()) +
                '}';
    }
}
