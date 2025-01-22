package com.example.gridcache.scheduler;

import com.example.gridcache.service.DataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataCacheSchedulerTest {

    @Mock
    private DataService dataService;

    @InjectMocks
    private DataCacheScheduler dataCacheScheduler;

    @Test
    void testRefreshCache() {
        // Arrange: Ensure the refreshCache method behaves as expected
        doNothing().when(dataService).refreshCache();  // Mocking the behavior of dataService.refreshCache()

        // Act: Call the refreshCache method
        dataCacheScheduler.refreshCache();

        // Assert: Verify the refreshCache method of DataService was called once
        verify(dataService, times(1)).refreshCache();
    }

    @Test
    void testRefreshCacheWithException() {
        // Arrange: Simulate an exception when calling refreshCache on DataService
        doThrow(new RuntimeException("Cache refresh failed")).when(dataService).refreshCache();

        // Act: Call the refreshCache method
        dataCacheScheduler.refreshCache();

        // Assert: Verify that the refreshCache method was called and exception handling occurs
        verify(dataService, times(1)).refreshCache();
    }
}
