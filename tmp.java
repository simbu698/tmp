Project Structure
css
Copy
Edit
spring-boot-cassandra/
│── src/main/java/com/example/demo/
│   ├── SpringBootCassandraApplication.java
│   ├── config/
│   │   ├── CassandraConfig.java
│   ├── controller/
│   │   ├── StudentController.java
│   ├── entity/
│   │   ├── Student.java
│   ├── repository/
│   │   ├── StudentRepository.java
│   ├── service/
│   │   ├── StudentService.java
│── src/main/resources/
│   ├── application.properties
│── pom.xml
1. pom.xml (Dependencies)
xml
Copy
Edit
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>spring-boot-cassandra</artifactId>
    <version>1.0.0</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.2</version>
        <relativePath/> 
    </parent>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Data Cassandra -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-cassandra</artifactId>
        </dependency>

        <!-- Lombok (Optional, for reducing boilerplate) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
2. application.properties (Cassandra Configuration)
properties
Copy
Edit
# Cassandra Configuration
spring.data.cassandra.contact-points=127.0.0.1
spring.data.cassandra.port=9042
spring.data.cassandra.keyspace-name=school
spring.data.cassandra.local-datacenter=datacenter1
spring.data.cassandra.username=cassandra
spring.data.cassandra.password=cassandra
spring.data.cassandra.schema-action=CREATE_IF_NOT_EXISTS
3. SpringBootCassandraApplication.java (Main Class)
java
Copy
Edit
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootCassandraApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootCassandraApplication.class, args);
    }
}
4. CassandraConfig.java (Configuration for Cassandra and SSL)
java
Copy
Edit
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.datastax.oss.driver.api.core.CqlSession;
import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9042;
    private static final String DATACENTER = "datacenter1";
    private static final String KEYSPACE = "school";
    private static final String USERNAME = "cassandra";
    private static final String PASSWORD = "cassandra";

    @Bean
    public CqlSession session() throws Exception {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(HOST, PORT))
                .withAuthCredentials(USERNAME, PASSWORD)
                .withKeyspace(KEYSPACE)
                .withLocalDatacenter(DATACENTER)
                .withSslContext(getSSLContext()) // Add SSL
                .build();
    }

    public static SSLContext getSSLContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, new java.security.SecureRandom());
        return sslContext;
    }
}
5. Student.java (Entity Class)
java
Copy
Edit
package com.example.demo.entity;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.Data;

@Table("students")
@Data
public class Student {
    
    @PrimaryKey
    private int id;
    private String name;
    private int age;
    private String grade;
}
6. StudentRepository.java (Repository Interface)
java
Copy
Edit
package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends CassandraRepository<Student, Integer> {
    List<Student> findAll();
}
7. StudentService.java (Service Layer)
java
Copy
Edit
package com.example.demo.service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.demo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private CqlSession session;

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        ResultSet resultSet = session.execute("SELECT id, name, age, grade FROM students");

        for (Row row : resultSet) {
            Student student = new Student();
            student.setId(row.getInt("id"));
            student.setName(row.getString("name"));
            student.setAge(row.getInt("age"));
            student.setGrade(row.getString("grade"));
            students.add(student);
        }
        return students;
    }
}
8. StudentController.java (REST Controller)
java
Copy
Edit
package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getAllStudents();
    }
}
