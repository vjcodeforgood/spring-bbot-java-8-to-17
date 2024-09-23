package com.example.springbootemployeecrud.service;

import com.example.springbootemployeecrud.entity.Employee;
import com.example.springbootemployeecrud.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value = "employees")
    public List<Employee> getAllEmployees() {
        long startTime = System.currentTimeMillis();
        List<Employee> employees = employeeRepository.findAll();
        long endTime = System.currentTimeMillis();
        logger.info("getAllEmployees execution time: {} ms", endTime - startTime);
        return employees;
    }

    @Cacheable(value = "employees", key = "#id")
    public Optional<Employee> getEmployeeById(Long id) {
        long startTime = System.currentTimeMillis();
        Optional<Employee> employee = employeeRepository.findById(id);
        long endTime = System.currentTimeMillis();
        logger.info("getEmployeeById execution time: {} ms", endTime - startTime);
        return employee;
    }

    @Caching(evict = {
            @CacheEvict(value = "employees", allEntries = true),
            @CacheEvict(value = "employees", key = "#employee.id")
    })
    public Employee createEmployee(Employee employee) {
        long startTime = System.currentTimeMillis();
        Employee createdEmployee = employeeRepository.save(employee);
        long endTime = System.currentTimeMillis();
        logger.info("createEmployee execution time: {} ms", endTime - startTime);
        return createdEmployee;
    }

    @Caching(evict = {
            @CacheEvict(value = "employees", allEntries = true),
            @CacheEvict(value = "employees", key = "#id")
    })
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        long startTime = System.currentTimeMillis();
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            existingEmployee.setName(employeeDetails.getName());
            existingEmployee.setEmail(employeeDetails.getEmail());
            existingEmployee.setDepartment(employeeDetails.getDepartment());
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            long endTime = System.currentTimeMillis();
            logger.info("updateEmployee execution time: {} ms", endTime - startTime);
            return updatedEmployee;
        }
        long endTime = System.currentTimeMillis();
        logger.info("updateEmployee execution time: {} ms", endTime - startTime);
        return null;
    }

    @Caching(evict = {
            @CacheEvict(value = "employees", allEntries = true),
            @CacheEvict(value = "employees", key = "#id")
    })
    public void deleteEmployee(Long id) {
        long startTime = System.currentTimeMillis();
        employeeRepository.deleteById(id);
        long endTime = System.currentTimeMillis();
        logger.info("deleteEmployee execution time: {} ms", endTime - startTime);
    }
}