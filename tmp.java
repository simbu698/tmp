Steps to Implement:
Setup Spring Boot and Dependencies
Add the required dependencies in pom.xml.
Configure Cassandra DataSources
Define separate configurations for each keyspace.
Create Repository and Entity Classes
Define entities and repositories for both keyspaces.
Service Layer
Implement a service to retrieve data.
Controller Layer
Expose REST APIs to fetch data.
1. Add Dependencies (pom.xml)
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

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>

</dependencies>
2. Configure Cassandra for Two Keyspaces
Create two separate configuration classes.

CassandraConfigKeyspaceOne.java
java
Copy
Edit
@Configuration
@EnableCassandraRepositories(basePackages = "com.example.repo.keyspace1",
        cassandraTemplateRef = "keyspaceOneTemplate")
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
@Configuration
@EnableCassandraRepositories(basePackages = "com.example.repo.keyspace2",
        cassandraTemplateRef = "keyspaceTwoTemplate")
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
@Repository
public interface UserRepositoryKeyspaceOne extends CassandraRepository<UserKeyspaceOne, UUID> {
}
Keyspace 2 - Entity (UserKeyspaceTwo.java)
java
Copy
Edit
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
@Repository
public interface UserRepositoryKeyspaceTwo extends CassandraRepository<UserKeyspaceTwo, UUID> {
}
4. Create Service Layer
java
Copy
Edit
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
java
Copy
Edit
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
Running the Application
Ensure Cassandra is running (cqlsh should be accessible).
Create required keyspaces and tables:
sql
Copy
Edit
CREATE KEYSPACE keyspace_one WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
CREATE TABLE keyspace_one.users (id UUID PRIMARY KEY, name text, age int);

CREATE KEYSPACE keyspace_two WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
CREATE TABLE keyspace_two.customers (id UUID PRIMARY KEY, name text, email text);
Start the Spring Boot application.
Testing the API
Get users from keyspace_one:
bash
Copy
Edit
GET http://localhost:8080/users/keyspace1
Get users from keyspace_two:
bash
Copy
Edit
GET http://localhost:8080/users/keyspace2
