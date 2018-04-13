package org.vno.neo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.Id;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kk
 */
@NodeEntity(label = "commit")
public class Commit {
    @Id
    Long id;

    @Property(name = "revision")
    private Long revision;

    @Property(name = "author_id")
    private Long authorId;

    private String message;

    private Long timestamp;

    @JsonIgnore
    @Relationship(type = "PARENT")
    private Set<Commit> parents;

    private Set<Long> parentIds;

    private Set<Long> blobIds;

    public Commit() {}

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

    public Set<Commit> getParents() {
        return parents;
    }

    public void setParents(Set<Commit> parents) {
        this.parents = parents;
    }

    public Set<Long> getParentIds() {
        return null == parents ? null : parents.stream().mapToLong(
                Commit::getRevision).boxed().collect(Collectors.toSet());
    }

    public void setParentIds(Set<Long> parentIds) {
        this.parentIds = parentIds;
    }

    public Set<Long> getDtoParentIds() {
        return parentIds;
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
                ", parents=" + (parents == null ? "null" : parents) +
                ", parentIds=" + (getParentIds() == null ? "null" :
                getParentIds()) +
                ", blobIds=" + (blobIds == null ? "null" : blobIds) +
                '}';
    }

}
