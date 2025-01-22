import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class DataControllerTest {

    @InjectMocks
    private DataController dataController; // The controller to be tested

    @Mock
    private DataService dataService; // Mocked service dependency

    private List<DataDetails> mockDataDetailsList;

    @BeforeEach
    public void setUp() {
        mockDataDetailsList = new ArrayList<>();
        mockDataDetailsList.add(new DataDetails(1, "Sample Data")); // Sample data initialization
    }

    @Test
    public void testGetAllData_success() {
        // Arrange
        when(dataService.getData()).thenReturn(mockDataDetailsList);

        // Act
        ResponseEntity<List<DataDetails>> response = dataController.getAllData();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDataDetailsList, response.getBody());
    }

    @Test
    public void testGetAllData_noData() {
        // Arrange
        when(dataService.getData()).thenReturn(null);

        // Act
        ResponseEntity<List<DataDetails>> response = dataController.getAllData();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetAllData_exception() {
        // Arrange
        when(dataService.getData()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<List<DataDetails>> response = dataController.getAllData();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
