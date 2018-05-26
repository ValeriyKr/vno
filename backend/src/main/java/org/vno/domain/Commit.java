package org.vno.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author kk
 */
@Entity
public class Commit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long revision;
    @NotNull
    @ManyToOne
    private UserAccount author;
    private String message;
    @NotNull
    private Long timestamp;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "parents",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "revision"))
    private Set<Commit> parents;
    @OneToMany(mappedBy = "revision")
    private Set<Blob> blobs;

    public Commit() {}

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public UserAccount getAuthor() {
        return author;
    }

    public void setAuthor(UserAccount author) {
        this.author = author;
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

    public Set<Commit> getParents() {
        return parents;
    }

    public void setParents(Set<Commit> parents) {
        this.parents = parents;
    }

    public Set<Blob> getBlobs() {
        return blobs;
    }

    public void setBlobs(Set<Blob> blobs) {
        this.blobs = blobs;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
