package org.vno.neo.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.vno.neo.domain.Commit;

import java.util.List;

/**
 * @author kk
 */
public interface CommitRepository extends GraphRepository<Commit> {
    @Query("MATCH (n:commit {revision:{revision}}) RETURN n")
    Commit findByRevision(@Param("revision") Long revision);

    @Query("MATCH (n:commit {revision:{revision}})-[p:PARENT*]->(m) " +
            "RETURN (n)-[]->(m)")
    List<Commit> findByRevisionWithDependency(@Param("revision") Long revision);

    @Query("MATCH (n:commit) RETURN max(n.revision)")
    Long findMaxId();

    @Query("MATCH " +
            "(b:branch " +
            "{branch:{branch}})-[*]->(n:commit {revision:{revision}})-" +
            "[p:PARENT]->(m)" +
            "RETURN (n),(m)")
    List<Commit> findFromBranch(@Param("branch") Long branch,
                                @Param("revision") Long revision);
}
