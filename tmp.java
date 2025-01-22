package com.example.gridcache.repository;

import com.example.gridcache.model.MigData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private DataRepository dataRepository;

    @Test
    public void testGetGridData_ExceptionScenario() {
        // Given: Mock the behavior of the EntityManager and Query
        when(entityManager.createNativeQuery(anyString(), eq(MigData.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Database query failed"));

        // When: Calling the method to simulate the exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dataRepository.getGridData();
        });

        // Then: Verify the exception message
        assertEquals("Database query failed", exception.getMessage());
    }
}
