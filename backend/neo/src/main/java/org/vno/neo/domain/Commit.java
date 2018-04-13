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

    @Property(name = "root_id")
    private Long rootId;

    @JsonIgnore
    @Relationship(type = "PARENT")
    private Set<Commit> parents;

    private Set<Long> blobs;

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

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
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

    public Set<Long> getBlobs() {
        return blobs;
    }

    public void setBlobs(Set<Long> blobs) {
        this.blobs = blobs;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "revision=" + (revision == null ? "null" : revision) +
                ", authorId=" + (authorId == null ? "null" : authorId) +
                ", timestamp=" + (timestamp == null ? "null" : timestamp) +
                ", rootId=" + (rootId == null ? "null" : rootId) +
                ", parents=" + (parents == null ? "null" : parents) +
                ", parentIds=" + (getParentIds() == null ? "null" :
                getParentIds()) +
                ", blobs=" + (blobs == null ? "null" : blobs) +
                '}';
    }
}
