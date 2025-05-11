// EmployeeService.java
package com.airline.model.service;

import com.airline.model.dao.EmployeeDao;
import com.airline.model.entity.Employee;
import com.airline.model.entity.EmployeePosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class EmployeeService {
    private static final Logger logger = LogManager.getLogger(EmployeeService.class);
    private final EmployeeDao employeeDao;

    public EmployeeService() {
        this.employeeDao = new EmployeeDao();
    }

    public Optional<Employee> findById(Long id) {
        logger.info("Finding employee by id: {}", id);
        return employeeDao.findById(id);
    }

    public List<Employee> findAll() {
        logger.info("Finding all employees");
        return employeeDao.findAll();
    }

    public List<Employee> findByPosition(String positionName) {
        logger.info("Finding employees by position: {}", positionName);
        return employeeDao.findByPosition(positionName);
    }

    public List<Employee> findByCrewId(Long crewId) {
        logger.info("Finding employees by crew id: {}", crewId);
        return employeeDao.findByCrewId(crewId);
    }

    public Employee save(Employee employee) {
        logger.info("Saving employee: {}", employee);
        return employeeDao.save(employee);
    }

    public boolean update(Employee employee) {
        logger.info("Updating employee: {}", employee);
        return employeeDao.update(employee);
    }

    public boolean delete(Long id) {
        logger.info("Deleting employee with id: {}", id);
        return employeeDao.delete(id);
    }

    public boolean validateEmployeeData(Employee employee) {
        // Проверка корректности данных сотрудника
        boolean isNameValid = employee.getFirstName() != null && !employee.getFirstName().isEmpty() &&
                employee.getLastName() != null && !employee.getLastName().isEmpty();

        boolean isPositionValid = employee.getPosition() != null &&
                employee.getPosition().getId() != null;

        return isNameValid && isPositionValid;
    }
}