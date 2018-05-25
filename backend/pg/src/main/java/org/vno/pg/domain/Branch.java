package org.vno.pg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author kk
 */
@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long branch;
    @NotBlank
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    private Commit head;
    @JsonIgnore
    @ManyToOne
    private Repo repo;

    public Branch() {}

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

    public Commit getHead() {
        return head;
    }

    public void setHead(Commit head) {
        this.head = head;
    }

    public Repo getRepo() {
        return repo;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }
}
