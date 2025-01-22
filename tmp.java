import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerDaoImplTest {

    @InjectMocks
    private CustomerDaoImpl customerDaoImpl; // Replace with your actual class name

    @Mock
    private EntityManager h2hEm;

    @Mock
    private Query mockQuery;

    @BeforeEach
    void setUp() {
        customerDaoImpl = new CustomerDaoImpl(); // Ensure the right class is initialized
    }

    @Test
    void testGetNoOfCustomers_success() {
        String testQuery = "SELECT COUNT(*) FROM customers";
        Long expectedResult = 100L;

        when(h2hEm.createNativeQuery(testQuery, Long.class)).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(expectedResult);

        Long actualResult = customerDaoImpl.getNoOfCustomers(testQuery);

        assertNotNull(actualResult, "Result should not be null");
        assertEquals(expectedResult, actualResult, "The number of customers should match the expected result");

        verify(h2hEm).createNativeQuery(testQuery, Long.class);
        verify(mockQuery).getSingleResult();
    }

    @Test
    void testGetNoOfCustomers_exceptionHandling() {
        String testQuery = "INVALID QUERY";

        when(h2hEm.createNativeQuery(testQuery, Long.class)).thenThrow(new RuntimeException("Database error"));

        Long result = customerDaoImpl.getNoOfCustomers(testQuery);

        assertNull(result, "Result should be null when an exception occurs");
    }
}
