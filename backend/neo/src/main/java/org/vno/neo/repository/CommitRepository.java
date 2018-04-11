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
    @Query("MATCH (n:commit {revision:{revision}})-[p:PARENT*0..1]->(m) " +
            "RETURN (n)-[]->(m)")
    List<Commit> findByRevision(@Param("revision") Long revision);
}
