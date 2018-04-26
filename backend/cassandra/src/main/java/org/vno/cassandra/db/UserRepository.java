package org.vno.cassandra.db;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class UserRepository {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Session session;

    @Autowired
    public UserRepository(Session session) {
        this.session = session;
        assert null != session;
    }

    public void add(Long userId, Long repoId, Long... branchIds) {
        StringBuilder sb = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS user_")
                .append(userId)
                .append("_")
                .append(repoId)
                .append(" (commit int PRIMARY KEY");
        if (null != branchIds) {
            for (Long branchId : branchIds) {
                sb.append(", commits_")
                        .append(branchId)
                        .append(" int");
            }
        }
        sb.append(");");
        session.execute(sb.toString());
    }

    public void commit(Long userId, Long repoId, Long branchId, Long revision) {
        StringBuilder sb = new StringBuilder()
                .append("INSERT INTO user_")
                .append(userId)
                .append("_")
                .append(repoId)
                .append(" (commit, commits_")
                .append(branchId)
                .append(") VALUES (")
                .append(revision)
                .append(",")
                .append(revision)
                .append(");");
        session.execute(sb.toString());
    }

    public void alter(Long userId, Long repoId, Long branchId) {
        StringBuilder sb = new StringBuilder()
                .append("ALTER TABLE user_")
                .append(userId)
                .append("_")
                .append(repoId)
                .append(" ADD commits_")
                .append(branchId)
                .append(" int;");
        session.execute(sb.toString());
    }
}
