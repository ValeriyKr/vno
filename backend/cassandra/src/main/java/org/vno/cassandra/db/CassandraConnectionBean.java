package org.vno.cassandra.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.logging.Logger;

/**
 * @author kk
 */
@Component
public class CassandraConnectionBean {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Value("${cassandra.host}")
    private String cassandraHost;

    @Value("${cassandra.port}")
    private Integer cassandraPort;

    private Cluster cluster;
    private Session session;

    @PostConstruct
    void init() {
        logger.info("Trying to establish cassandra connection " +
                cassandraHost + ":" + cassandraPort);
        try {
            cluster = Cluster.builder().addContactPoint(cassandraHost)
                    .withPort(cassandraPort).build();
            session = cluster.connect();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        logger.info("Successfully connected");
        session.execute("CREATE KEYSPACE IF NOT EXISTS vno WITH replication " +
                "= {'class':'SimpleStrategy','replication_factor':1};");
        session.execute("USE vno");
    }

    @PreDestroy
    void destroy() {
        logger.info("Destroying cassandra connection");
        session.close();
        cluster.close();
    }

    public Session getSession() {
        return session;
    }
}
