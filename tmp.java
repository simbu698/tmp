package com.example.gridcache.service;

import com.example.gridcache.model.MigData;
import com.example.gridcache.repository.DataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DataServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataService dataService;

    @Test
    void testRefreshCache() {
        // Arrange
        List<MigData> mockData = Arrays.asList(new MigData(), new MigData());
        when(dataRepository.getGridData()).thenReturn(mockData);

        // Act
        List<MigData> result = dataService.refreshCache();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dataRepository, times(1)).getGridData();
    }

    @Test
    void testRefreshCacheThrowsException() {
        // Arrange
        when(dataRepository.getGridData()).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> dataService.refreshCache());
        assertEquals("Error while refreshing cache", exception.getMessage());
    }

    @Test
    void testGetDataFromCache() {
        // Arrange
        List<MigData> mockData = Arrays.asList(new MigData(), new MigData());
        when(dataRepository.getCachedData()).thenReturn(mockData);

        // Act
        List<MigData> result = dataService.getDataFromCache();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dataRepository, times(1)).getCachedData();
    }
}
