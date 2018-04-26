package org.vno.cassandra.db;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author kk
 */
@Component
public class BranchRepository {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Session session;

    @Autowired
    public BranchRepository(Session session) {
        this.session = session;
        assert null != session;
    }

    public void add(Long repoId, Long branchId, Long... userIds) {
        StringBuilder sb = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS branch_")
                .append(repoId)
                .append("_")
                .append(branchId)
                .append(" (commit bigint PRIMARY KEY");
        if (null != userIds) {
            for (Long userId : userIds) {
                sb.append(", commits_")
                        .append(userId)
                        .append(" bigint");
            }
        }
        sb.append(");");
        session.execute(sb.toString());
    }

    public void commit(Long userId, Long repoId, Long branchId, Long revision) {
        StringBuilder sb = new StringBuilder()
                .append("INSERT INTO branch_")
                .append(repoId)
                .append("_")
                .append(branchId)
                .append(" (commit, commits_")
                .append(userId)
                .append(") VALUES (")
                .append(revision)
                .append(",")
                .append(revision)
                .append(");");
        session.execute(sb.toString());
    }

    public void alter(Long repoId, Long branchId, Long userId) {
        StringBuilder sb = new StringBuilder()
                .append("ALTER TABLE branch_")
                .append(repoId)
                .append("_")
                .append(branchId)
                .append(" ADD commits_")
                .append(userId)
                .append(" bigint;");
        session.execute(sb.toString());
    }

    public List<Long> slice(Long repoId, Long branchId) {
        StringBuilder sb = new StringBuilder()
                .append("SELECT commit FROM branch_")
                .append(repoId)
                .append("_")
                .append(branchId)
                .append(";");
        return session.execute(sb.toString()).all().stream()
                .map(r -> r.get(0, Long.class)).collect(Collectors.toList());
    }
}
