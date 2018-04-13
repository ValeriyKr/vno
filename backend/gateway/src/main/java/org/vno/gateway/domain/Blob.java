package org.vno.gateway.domain;

import java.util.Objects;

/**
 * @author kk
 */
public class Blob {
    private Long id;
    private String name;
    private String content;
    private Integer mode;

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
