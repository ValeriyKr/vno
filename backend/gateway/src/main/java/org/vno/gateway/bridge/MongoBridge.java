package org.vno.gateway.bridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.vno.gateway.domain.Role;
import org.vno.gateway.domain.UserAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kk
 */
@Component
public class MongoBridge {
    @Value("${mongo.server.url}")
    private String url;

    public UserAccount getUserByUsername(String username) {
        return new RestTemplate().getForObject(url + "/user/get/" + username,
                UserAccount.class);
    }

    public List<UserAccount> getAll() {
        List<UserAccount> users = new ArrayList<>();
        users = new RestTemplate().getForObject(url + "/user/all/",
                users.getClass());
        return users;
    }

    public Role getRoleById(Long id) {
        return new RestTemplate().getForObject(url + "/role/get/" + id,
                Role.class);
    }
}
