package org.vno.neo.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.vno.neo.domain.Tag;
import org.vno.neo.domain.Commit;

import java.util.Set;

/**
 * @author kk
 */
public interface TagRepository extends GraphRepository<Tag> {
    Tag findByTag(Long tag);

    @Query("MATCH (n:tag {id:{id}})-[p:POINTS]->(m) " +
            "RETURN (m)")
    Commit findHead(@Param("id") Long id);

    @Query("MATCH (n:tag) RETURN max(n.tag)")
    Long findMaxId();

    Set<Tag> findByNameAndTagIn(String name, Set<Long> ids);
}
