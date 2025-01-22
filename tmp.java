package com.example.gridcache.repository;

import com.example.gridcache.model.MigData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
public class DataRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DataRepository dataRepository;

    @Test
    public void testGetGridData() {
        // Prepare mock data
        MigData migData1 = new MigData();
        migData1.setPId(1);
        migData1.setFileCount(5);

        MigData migData2 = new MigData();
        migData2.setPId(2);
        migData2.setFileCount(10);

        List<MigData> expectedData = Arrays.asList(migData1, migData2);

        // Mock behavior of EntityManager and Query
        when(entityManager.createNativeQuery(anyString(), eq(MigData.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedData);

        // Call the method
        List<MigData> result = dataRepository.getGridData();

        // Validate the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getPId());
        assertEquals(5, result.get(0).getFileCount());
        assertEquals(2, result.get(1).getPId());
        assertEquals(10, result.get(1).getFileCount());

        // Verify the interactions
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(MigData.class));
        verify(query, times(1)).getResultList();
    }

    @Test
    public void testGetCachedData() {
        // Test the cached data method (which calls getGridData)
        List<MigData> cachedData = dataRepository.getCachedData();
        assertNotNull(cachedData);
    }
}
