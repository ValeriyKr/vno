package org.vno.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author kk
 */
public class Branch {
    @JsonIgnore
    private Long id;
    private Long branch;
    private String name;
    private Long head;

    public Branch() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBranch() {
        return branch;
    }

    public void setBranch(Long branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHead() {
        return head;
    }

    public void setHead(Long head) {
        this.head = head;
    }
}
