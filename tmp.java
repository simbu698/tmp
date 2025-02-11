1. Fix Cassandra Configuration for Keyspace 1
java
Copy
Edit
package com.example.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.nio.file.Paths;

@Configuration
@EnableCassandraRepositories(
        basePackages = "com.example.repository.keyspace1",
        cassandraTemplateRef = "keyspaceOneTemplate"
)
public class CassandraConfigKeyspaceOne extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name-one}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String datacenter;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Bean(name = "keyspaceOneSession")
    public CqlSessionFactoryBean session() {
        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints(contactPoints);
        session.setPort(port);
        session.setLocalDatacenter(datacenter);
        session.setKeyspaceName(keyspaceName);
        return session;
    }

    @Bean(name = "keyspaceOneTemplate")
    public CassandraTemplate cassandraTemplateOne(CqlSession keyspaceOneSession) {
        return new CassandraTemplate(keyspaceOneSession);
    }
}
2. Fix Cassandra Configuration for Keyspace 2
java
Copy
Edit
package com.example.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(
        basePackages = "com.example.repository.keyspace2",
        cassandraTemplateRef = "keyspaceTwoTemplate"
)
public class CassandraConfigKeyspaceTwo extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name-two}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String datacenter;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Bean(name = "keyspaceTwoSession")
    public CqlSessionFactoryBean session() {
        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints(contactPoints);
        session.setPort(port);
        session.setLocalDatacenter(datacenter);
        session.setKeyspaceName(keyspaceName);
        return session;
    }

    @Bean(name = "keyspaceTwoTemplate")
    public CassandraTemplate cassandraTemplateTwo(CqlSession keyspaceTwoSession) {
        return new CassandraTemplate(keyspaceTwoSession);
    }
}
3. Update Repositories
Make sure your repository interfaces are inside the correct packages as specified in the @EnableCassandraRepositories annotation.

Keyspace 1 - Repository (UserRepositoryKeyspaceOne.java)
java
Copy
Edit
package com.example.repository.keyspace1;

import com.example.entity.UserKeyspaceOne;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepositoryKeyspaceOne extends CassandraRepository<UserKeyspaceOne, UUID> {
}
Keyspace 2 - Repository (UserRepositoryKeyspaceTwo.java)
java
Copy
Edit
package com.example.repository.keyspace2;

import com.example.entity.UserKeyspaceTwo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepositoryKeyspaceTwo extends CassandraRepository<UserKeyspaceTwo, UUID> {
}
4. Update application.properties
Ensure that your application properties match your database setup.

properties
Copy
Edit
spring.data.cassandra.contact-points=127.0.0.1
spring.data.cassandra.port=9042
spring.data.cassandra.local-datacenter=datacenter1

# Keyspace One
spring.data.cassandra.keyspace-name-one=keyspace_one

# Keyspace Two
spring.data.cassandra.keyspace-name-two=keyspace_two
