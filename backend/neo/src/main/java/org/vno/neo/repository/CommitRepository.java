package org.vno.neo.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.vno.neo.domain.Commit;

import java.util.List;

/**
 * @author kk
 */
public interface CommitRepository extends GraphRepository<Commit> {
    Commit findByRevision(Long revision);

    //@Query("MATCH (e:commit) RETURN e")
    //List<Commit> findAll();
}
