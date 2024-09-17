import com.example.springbootemployeecrud.entity.Employee;
import com.example.springbootemployeecrud.repository.EmployeeRepository;
import com.example.springbootemployeecrud.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() {
        // Arrange
        Employee emp1 = new Employee("John Doe", "john@example.com", "IT");
        Employee emp2 = new Employee("Jane Doe", "jane@example.com", "HR");
        List<Employee> expectedEmployees = Arrays.asList(emp1, emp2);

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // Act
        List<Employee> actualEmployees = employeeService.getAllEmployees();

        // Assert
        assertEquals(expectedEmployees, actualEmployees);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_withValidId_shouldReturnEmployee() {
        // Arrange
        Long id = 1L;
        Employee expectedEmployee = new Employee("John Doe", "john@example.com", "IT");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(expectedEmployee));

        // Act
        Optional<Employee> actualEmployee = employeeService.getEmployeeById(id);

        // Assert
        assertTrue(actualEmployee.isPresent());
        assertEquals(expectedEmployee, actualEmployee.get());
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void getEmployeeById_withInvalidId_shouldReturnEmpty() {
        // Arrange
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Employee> actualEmployee = employeeService.getEmployeeById(id);

        // Assert
        assertFalse(actualEmployee.isPresent());
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void createEmployee_shouldReturnCreatedEmployee() {
        // Arrange
        Employee employeeToCreate = new Employee("John Doe", "john@example.com", "IT");
        when(employeeRepository.save(employeeToCreate)).thenReturn(employeeToCreate);

        // Act
        Employee createdEmployee = employeeService.createEmployee(employeeToCreate);

        // Assert
        assertEquals(employeeToCreate, createdEmployee);
        verify(employeeRepository, times(1)).save(employeeToCreate);
    }

    @Test
    void updateEmployee_withValidId_shouldReturnUpdatedEmployee() {
        // Arrange
        Long id = 1L;
        Employee existingEmployee = new Employee("John Doe", "john@example.com", "IT");
        Employee updatedEmployeeDetails = new Employee("John Updated", "john@example.com", "HR");
        
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Employee updatedEmployee = employeeService.updateEmployee(id, updatedEmployeeDetails);

        // Assert
        assertNotNull(updatedEmployee);
        assertEquals(updatedEmployeeDetails.getName(), updatedEmployee.getName());
        assertEquals(updatedEmployeeDetails.getDepartment(), updatedEmployee.getDepartment());
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_withInvalidId_shouldReturnNull() {
        // Arrange
        Long id = 1L;
        Employee updatedEmployeeDetails = new Employee("John Updated", "john@example.com", "HR");
        
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Employee updatedEmployee = employeeService.updateEmployee(id, updatedEmployeeDetails);

        // Assert
        assertNull(updatedEmployee);
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_shouldCallRepositoryDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        employeeService.deleteEmployee(id);

        // Assert
        verify(employeeRepository, times(1)).deleteById(id);
    }
}
