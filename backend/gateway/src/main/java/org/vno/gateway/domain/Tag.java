package org.vno.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author kk
 */
public class Tag {
    @JsonIgnore
    private Long id;
    private Long tag;
    private String name;
    private Long head;

    public Tag() {}

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

    public void setHead(Long head) {
        this.head = head;
    }

    public Long getHead() {
        return head;
    }

}
