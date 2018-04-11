package org.vno.mongo.domain;

import org.springframework.data.annotation.Id;

/**
 * @author kk
 */
public class Role {
    @Id
    String objectId;

    Long id;
    String name;

    public Role() {}

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
