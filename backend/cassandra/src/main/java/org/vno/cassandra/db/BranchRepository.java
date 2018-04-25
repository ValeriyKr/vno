package org.vno.cassandra.db;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class BranchRepository {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final CassandraConnectionBean repository;

    private Session session;

    @Autowired
    public BranchRepository(CassandraConnectionBean cassandraConnectionBean) {
        this.repository = cassandraConnectionBean;

        session = cassandraConnectionBean.getSession();
    }

    public void add(Long repoId, Long branchId) {
        StringBuilder sb = new StringBuilder()
                .append("CREATE TABLE branch_")
                .append(repoId)
                .append("_")
                .append(branchId)
                .append(" (commit int PRIMARY KEY);");
        session.execute(sb.toString());
    }

}
