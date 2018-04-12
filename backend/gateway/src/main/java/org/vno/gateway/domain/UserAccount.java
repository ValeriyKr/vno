package org.vno.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * @author kk
 */
public class UserAccount {
    @JsonIgnore
    public String objectId;

    public Long id;

    public String username;
    public String firstname;
    public String lastname;
    public String email;
    public String password;

    public Set<Long> roleIds;
    public Set<Long> repoIds;

    public UserAccount() {}

    public UserAccount(String objectId, Long id, String username, String firstname,
                       String lastname, String email, String password,
                       Set<Long> roleIds, Set<Long> repoIds) {
        this.objectId = objectId;
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.roleIds = roleIds;
        this.repoIds = repoIds;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<Long> getRepoIds() {
        return repoIds;
    }

    public void setRepoIds(Set<Long> repoIds) {
        this.repoIds = repoIds;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + (id == null ? "null" : id) +
                ", username='" + (username == null ? "null" : username) + '\'' +
                ", firstname='" + (firstname == null ? "null" : firstname) + '\'' +
                ", lastname='" + (lastname == null ? "null" : lastname) + '\'' +
                ", email='" + (email == null ? "null" : email) + '\'' +
                ", password='" + (password == null ? "null" : password) + '\'' +
                ", roleIds=" + (roleIds == null ? "null" : roleIds.toString()) +
                ", branchIds=" + (repoIds == null ? "null" : repoIds.toString()) +
                '}';
    }
}
