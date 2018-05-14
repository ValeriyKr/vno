package org.vno.pg.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author kk
 */
@Entity
public class Blob {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    private String content;
    @NotNull
    private Integer mode;
    @ManyToOne
    @JoinColumn(name = "revision")
    @NotNull
    private Commit revision;

    public Blob() {}

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Commit getRevision() {
        return revision;
    }

    public void setRevision(Commit revision) {
        this.revision = revision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blob blob = (Blob) o;
        return Objects.equals(name, blob.name) &&
                Objects.equals(content, blob.content) &&
                Objects.equals(mode, blob.mode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, content, mode);
    }
}
