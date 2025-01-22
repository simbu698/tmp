package com.example.gridcache.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CacheConfigTest {

    @InjectMocks
    private CacheConfig cacheConfig;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        // Optionally, initialize the mock behavior if needed
        when(cacheManager.getCacheNames()).thenReturn(Set.of("dataGridCache"));
    }

    @Test
    public void testCacheManager() {
        // Use the cacheManager from the CacheConfig
        CacheManager cacheManager = cacheConfig.cacheManager();

        // Verify that the cacheManager is not null
        assertNotNull(cacheManager, "CacheManager should not be null");

        // Optionally, verify if the mock behavior was invoked (if it's useful)
        verify(this.cacheManager, times(1)).getCacheNames();
    }
}
