package org.vno.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author kk
 */
@Entity
public class Repo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;
    private String description;

    @OneToMany(mappedBy = "repo")
    private Set<Branch> branches;
    @OneToMany(mappedBy = "repo")
    private Set<Tag> tags;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "repos")
    private Set<UserAccount> owners;

    public Repo() {}

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

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<UserAccount> getOwners() {
        return owners;
    }

    public void setOwners(Set<UserAccount> owners) {
        this.owners = owners;
    }
}
