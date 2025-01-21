package com.example.gridcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class GridCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(GridCacheApplication.class, args);
    }
}

---

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
import java.util.Map;

@Service
public class DataService {

    private final DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @CachePut(value = "dataGridCache")
    public List<Map<String, Object>> refreshCache() {
        try {
            return dataRepository.getGridData();
        } catch (Exception e) {
            throw new RuntimeException("Error while refreshing cache", e);
        }
    }

    @Cacheable(value = "dataGridCache")
    public List<Map<String, Object>> getDataFromCache() {
        return dataRepository.getCachedData();
    }
}

---

package com.example.gridcache.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataRepository {

    private final JdbcTemplate jdbcTemplate;

    public DataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getGridData() {
        try {
            String sql = "SELECT * FROM your_large_data_query";  // Replace with actual optimized query
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    public List<Map<String, Object>> getCachedData() {
        // Direct cache retrieval is handled by service-level caching, this method can be simplified or removed.
        throw new UnsupportedOperationException("This method is now unused");
    }
}

---

package com.example.gridcache.controller;

import com.example.gridcache.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GridController {

    private final DataService dataService;

    public GridController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/data")
    public List<Map<String, Object>> getData() {
        return dataService.getDataFromCache();
    }
}
