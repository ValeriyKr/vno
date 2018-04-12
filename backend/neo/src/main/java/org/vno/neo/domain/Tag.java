package org.vno.neo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.Id;

/**
 * @author kk
 */
@NodeEntity(label = "tag")
public class Tag {
    @Id
    @JsonIgnore
    private Long id;

    private Long tag;

    private String name;

    @JsonIgnore
    @Relationship(type = "POINTS")
    private Commit commit;

    Tag() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public Long getHead() {
        return null == commit ? null : commit.getRevision();
    }

}
