package org.vno.gateway.bridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.vno.gateway.domain.Blob;
import org.vno.gateway.domain.Repo;
import org.vno.gateway.domain.Role;
import org.vno.gateway.domain.UserAccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class MongoBridge {
    Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${mongo.server.url}")
    private String url;

    public UserAccount getUserByUsername(String username) {
        return new RestTemplate().getForObject(url + "/user/get/" + username,
                UserAccount.class);
    }

    public HttpStatus testUsernameOrEmail(String username, String email) {
        try {
            return new RestTemplate().getForEntity(url + "/user/get/" +
                    username + "/" + email, UserAccount.class).getStatusCode();
        } catch (Exception e) {
            return HttpStatus.NOT_FOUND;
        }
    }

    public UserAccount getUserById(Long id) {
        try {
            return new RestTemplate().getForObject(url + "/user/get_by_id/"
                    + id, UserAccount.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserAccount> getAllUsers() {
        List<UserAccount> users = new ArrayList<>();
        users = new RestTemplate().getForObject(url + "/user/all/",
                users.getClass());
        return users;
    }

    public Role getRoleById(Long id) {
        return new RestTemplate().getForObject(url + "/role/get/" + id,
                Role.class);
    }

    public Role getRoleByName(String name) {
        return new RestTemplate().getForObject(url + "/role/get_by_name/" +
                name, Role.class);
    }

    public Repo getRepoById(Long id) {
        return new RestTemplate().getForObject(url + "/repo/get/" + id,
                Repo.class);
    }

    public ResponseEntity<?> add(UserAccount user) {
        try {
            logger.info("Add user: " + user.toString());
            return new RestTemplate().postForEntity(url + "/user/add", user,
                    UserAccount.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>("Already exists",
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> addRepo(Repo repo_, UserAccount owner) {
        return new RestTemplate().postForEntity(url + "/repo/add",
                new Serializable() {
                    Repo repo = repo_;
                    UserAccount user = owner;

                    public Repo getRepo() {
                        return repo;
                    }

                    public void setRepo(Repo repo) {
                        this.repo = repo;
                    }

                    public UserAccount getUser() {
                        return user;
                    }

                    public void setUser(UserAccount user) {
                        this.user = user;
                    }
                }, Repo.class);
    }

    public Repo getRepoByBranch(Long id) {
        return new RestTemplate().getForObject(url + "/repo/get_by_branch/"
                + id, Repo.class);
    }

    public Blob addBlob(Blob blob) {
        return new RestTemplate().postForEntity(url + "/blob/add", blob,
                Blob.class).getBody();
    }

    public ArrayList<Blob> getBlobsByIds(List<Blob> blobs) {
        ArrayList<Blob> rc = new ArrayList<>();
        rc = new RestTemplate().postForEntity(url + "/blob/batch/", blobs, rc.getClass()).getBody();
        return rc;
    }
}
