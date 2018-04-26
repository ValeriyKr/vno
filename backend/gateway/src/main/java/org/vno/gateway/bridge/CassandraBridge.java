package org.vno.gateway.bridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class CassandraBridge {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${cassandra.server.url}")
    private String url;

    public void addBranch(Long repoId, Long branchId, List<Long> userIds) {
        new RestTemplate().put(url + "/ref/" + repoId + "/" + branchId,
                userIds);
    }

    public void addUser(Long userId, Long repoId, List<Long> branchIds) {
        new RestTemplate().put(url + "/user/" + userId + "/" + repoId,
                branchIds);
    }

    public void addRevision(Long userId, Long repoId, Long branchId,
                            Long revision) {
        new RestTemplate().put(url + "/r/" + userId + "/" + repoId + "/" +
                branchId + "/" + revision, null);
    }

    @SuppressWarnings("unchecked")
    public List<Long> branchSlice(Long repoId, Long branchId) {
        return Arrays.asList(new RestTemplate().getForObject(
                url + "/ref/" + repoId + "/" + branchId, Long[].class));
    }
}

