package org.vno.mongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author kk
 */
@Document(collection = "repo")
public class Repo {
    @Id
    private String objectId;

    private Long id;

    private String name;
    private String description;

    @Field(value = "branch_ids")
    private List<Long> branchIds;
    @Field(value = "tag_ids")
    private List<Long> tagIds;

    public Repo() {}

    public Repo(String objectId, Long id, String name, String description,
                List<Long> branchIds, List<Long> tagIds) {
        this.objectId = objectId;
        this.id = id;
        this.name = name;
        this.description = description;
        this.branchIds = branchIds;
        this.tagIds = tagIds;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getBranchIds() {
        return branchIds;
    }

    public void setBranchIds(List<Long> branchIds) {
        this.branchIds = branchIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + (id == null ? "null" : id) +
                ", name='" + (name == null ? "null" : name) + '\'' +
                ", description='" + (description == null ? "null" : description) + '\'' +
                ", branchIds=" + (branchIds == null ? "null" : branchIds.toString()) +
                ", tagIds=" + (tagIds == null ? "null" : tagIds.toString()) +
                '}';
    }
}
