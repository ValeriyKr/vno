package org.vno.gateway.bridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.vno.gateway.domain.Branch;
import org.vno.gateway.domain.Tag;

import java.util.Set;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class NeoBridge {
    Logger logger = Logger.getLogger(this.getClass().getName());

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

}
