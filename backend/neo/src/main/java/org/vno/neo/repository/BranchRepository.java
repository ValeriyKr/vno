package org.vno.neo.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.vno.neo.domain.Branch;
import org.vno.neo.domain.Commit;

import java.util.List;

/**
 * @author kk
 */
public interface BranchRepository extends GraphRepository<Branch> {
    Branch findByBranch(Long branch);

    @Query("MATCH (n:branch {id:{id}})-[p:POINTS]->(m) " +
            "RETURN (m)")
    Commit findHead(@Param("id") Long id);
}
