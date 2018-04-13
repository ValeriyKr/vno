package org.vno.neo.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.vno.neo.domain.Branch;
import org.vno.neo.domain.Commit;

import java.util.Set;

/**
 * @author kk
 */
public interface BranchRepository extends GraphRepository<Branch> {
    Branch findByBranch(Long branch);

    @Query("MATCH (n:branch {id:{id}})-[p:POINTS]->(m) " +
            "RETURN (m)")
    Commit findHead(@Param("id") Long id);

    @Query("MATCH (n:branch) RETURN max(n.branch)")
    Long findMaxId();

    Set<Branch> findByNameAndBranchIn(String name, Set<Long> ids);

    @Query("MATCH (b:branch {id:{id}})-[p:POINTS]->(m)," +
            "(n:commit {revision:{head}}) DELETE p CREATE (b)-[t:POINTS]->(n)")
    void updateBranchHead(@Param("id") Long id, @Param("head") Long head);

}
