1. Dependencies (pom.xml)
xml
Copy
Edit
<dependencies>
    <!-- Spring Boot Starter Data Cassandra -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-cassandra</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Lombok for reducing boilerplate code -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
2. Configuration for Cassandra Keyspaces
CassandraConfigKeyspaceOne.java
java
Copy
Edit
package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfigKeyspaceOne extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name-one}")
    private String keyspaceName;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Bean(name = "keyspaceOneTemplate")
    public CassandraTemplate cassandraTemplateOne(CqlSession session) {
        return new CassandraTemplate(session);
    }
}
CassandraConfigKeyspaceTwo.java
java
Copy
Edit
package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfigKeyspaceTwo extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name-two}")
    private String keyspaceName;

    @Override
    protected String getKeyspaceName() {
        return keyspaceName;
    }

    @Bean(name = "keyspaceTwoTemplate")
    public CassandraTemplate cassandraTemplateTwo(CqlSession session) {
        return new CassandraTemplate(session);
    }
}
3. Define Entities and Repositories
Keyspace 1 - Entity (UserKeyspaceOne.java)
java
Copy
Edit
package com.example.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserKeyspaceOne {

    @PrimaryKey
    private UUID id;
    
    private String name;
    private int age;
}
Keyspace 1 - Repository (UserRepositoryKeyspaceOne.java)
java
Copy
Edit
package com.example.repository;

import com.example.entity.UserKeyspaceOne;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepositoryKeyspaceOne extends CassandraRepository<UserKeyspaceOne, UUID> {
}
Keyspace 2 - Entity (UserKeyspaceTwo.java)
java
Copy
Edit
package com.example.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("customers")
public class UserKeyspaceTwo {

    @PrimaryKey
    private UUID id;
    
    private String name;
    private String email;
}
Keyspace 2 - Repository (UserRepositoryKeyspaceTwo.java)
java
Copy
Edit
package com.example.repository;

import com.example.entity.UserKeyspaceTwo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepositoryKeyspaceTwo extends CassandraRepository<UserKeyspaceTwo, UUID> {
}
4. Create Service Layer
UserService.java
java
Copy
Edit
package com.example.service;

import com.example.entity.UserKeyspaceOne;
import com.example.entity.UserKeyspaceTwo;
import com.example.repository.UserRepositoryKeyspaceOne;
import com.example.repository.UserRepositoryKeyspaceTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepositoryKeyspaceOne userRepoOne;
    private final UserRepositoryKeyspaceTwo userRepoTwo;

    @Autowired
    public UserService(UserRepositoryKeyspaceOne userRepoOne, UserRepositoryKeyspaceTwo userRepoTwo) {
        this.userRepoOne = userRepoOne;
        this.userRepoTwo = userRepoTwo;
    }

    public List<UserKeyspaceOne> getAllUsersFromKeyspaceOne() {
        return userRepoOne.findAll();
    }

    public List<UserKeyspaceTwo> getAllUsersFromKeyspaceTwo() {
        return userRepoTwo.findAll();
    }
}
5. Create REST Controller
UserController.java
java
Copy
Edit
package com.example.controller;

import com.example.entity.UserKeyspaceOne;
import com.example.entity.UserKeyspaceTwo;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/keyspace1")
    public List<UserKeyspaceOne> getUsersFromKeyspaceOne() {
        return userService.getAllUsersFromKeyspaceOne();
    }

    @GetMapping("/keyspace2")
    public List<UserKeyspaceTwo> getUsersFromKeyspaceTwo() {
        return userService.getAllUsersFromKeyspaceTwo();
    }
}
6. Define Properties in application.properties
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
7. Create Keyspaces and Tables in Cassandra
Run the following queries in cqlsh:

sql
Copy
Edit
CREATE KEYSPACE keyspace_one WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
CREATE TABLE keyspace_one.users (id UUID PRIMARY KEY, name text, age int);

CREATE KEYSPACE keyspace_two WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
CREATE TABLE keyspace_two.customers (id UUID PRIMARY KEY, name text, email text);
8. Run the Application
Start your Spring Boot application and test with:

Get users from Keyspace 1
bash
Copy
Edit
GET http://localhost:8080/users/keyspace1
Get users from Keyspace 2
bash
Copy
Edit
GET http://localhost:8080/users/keyspace2
