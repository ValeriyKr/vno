package org.vno.gateway.bridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.vno.gateway.domain.Branch;
import org.vno.gateway.domain.Commit;
import org.vno.gateway.domain.Tag;

import java.util.Set;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class NeoBridge {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${neo4j.server.url}")
    private String url;

    public Branch getBranchById(Long id) {
        return new RestTemplate().getForObject(url + "/ref/get/" + id,
                Branch.class);
    }

    public Branch getBranchByNameWithIdIn(String name, Set<Long> possibleIds) {
        try {
            return new RestTemplate().postForObject(url +
                            "/ref/get_by_name_with_id_in/" + name,
                    possibleIds, Branch.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveBranch(Branch branch) {
        new RestTemplate().put(url + "/ref/add/", branch);
    }

    public void deleteBranch(Long branch) {
        new RestTemplate().delete(url + "/ref/del/" + branch);
    }

    public void moveBranch(Long branchId, Long revision) {
        new RestTemplate().postForLocation(url + "/ref/move/" + branchId + "/"
                + revision, null);
    }

    public Long getBranchHead(Long branchId) {
        return new RestTemplate().getForObject(url + "/ref/head/"
                + branchId, Long.class);
    }

    public Tag getTagById(Long id) {
        return new RestTemplate().getForObject(url + "/tag/get/" + id,
                Tag.class);
    }

    public Tag getTagByNameWithIdIn(String name, Set<Long> possibleIds) {
        try {
            return new RestTemplate().postForObject(url +
                            "/tag/get_by_name_with_id_in/" + name,
                    possibleIds, Tag.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveTag(Tag tag) {
        new RestTemplate().put(url + "/tag/add/", tag);
    }

    public void deleteTag(Long tag) {
        new RestTemplate().delete(url + "/tag/del/" + tag);
    }

    public Commit saveCommit(Commit commit) {
        ResponseEntity<Commit> rc = new RestTemplate()
                .exchange(url + "/r/", HttpMethod.PUT,
                        new HttpEntity<>(commit, new HttpHeaders()),
                        Commit.class);
        if (rc.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return rc.getBody();
    }

    public Commit getCommitFromBranch(Long branchId, Long revision) {
        return new RestTemplate().getForObject(url + "/r/" + branchId + "/"
                + revision, Commit.class);
    }

}
