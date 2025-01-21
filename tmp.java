package com.example.gridcache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("dataGridCache");
    }
}

---

package com.example.gridcache.scheduler;

import com.example.gridcache.service.DataService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataCacheScheduler {

    private final DataService dataService;

    public DataCacheScheduler(DataService dataService) {
        this.dataService = dataService;
    }

    @Scheduled(fixedRateString = "${scheduler.refresh.rate:600000}")  // Default to 10 minutes
    public void refreshCache() {
        try {
            log.info("Refreshing cache...");
            dataService.refreshCache();
        } catch (Exception e) {
            log.error("Failed to refresh cache: ", e);
        }
    }
}

---

package com.example.gridcache.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.gridcache.model.MigData;

@Service
public class DataService {

    private final DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @CachePut(value = "dataGridCache")
    public List<MigData> refreshCache() {
        try {
            return dataRepository.getGridData();
        } catch (Exception e) {
            throw new RuntimeException("Error while refreshing cache", e);
        }
    }

    @Cacheable(value = "dataGridCache")
    public List<MigData> getDataFromCache() {
        return dataRepository.getCachedData();
    }
}

---

package com.example.gridcache.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import com.example.gridcache.model.MigData;
import java.util.List;

@Repository
public class DataRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<MigData> getGridData() {
        try {
            String sql = "SELECT p_id AS pId, file_count AS fileCount FROM your_large_data_query";  // Replace with actual optimized query
            Query query = entityManager.createNativeQuery(sql, MigData.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    public List<MigData> getCachedData() {
        // Optional: Here, cache-based retrieval could be added if needed.
        return getGridData();
    }
}

---

package com.example.gridcache.controller;

import com.example.gridcache.service.DataService;
import com.example.gridcache.model.MigData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GridController {

    private final DataService dataService;

    public GridController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/data")
    public List<MigData> getData() {
        return dataService.getDataFromCache();
    }
}

---

package com.example.gridcache.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class MigData {

    @Id
    @Column(name = "p_id")
    private String pId;

    @Column(name = "file_count")
    private Long fileCount;

    // Getters and Setters

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public Long getFileCount() {
        return fileCount;
    }

    public void setFileCount(Long fileCount) {
        this.fileCount = fileCount;
    }
}
