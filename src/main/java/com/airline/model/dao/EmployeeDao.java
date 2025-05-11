package com.airline.model.dao;

import com.airline.config.ConnectionPool;
import com.airline.model.entity.Employee;
import com.airline.model.entity.EmployeePosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDao implements GenericDao<Employee, Long> {
    private static final Logger logger = LogManager.getLogger(EmployeeDao.class);
    private final ConnectionPool connectionPool;

    // SQL запросы
    private static final String FIND_BY_ID =
            "SELECT e.*, p.id as position_id, p.name as position_name, p.description as position_description " +
                    "FROM employees e JOIN employee_positions p ON e.position_id = p.id WHERE e.id = ?";
    private static final String FIND_ALL =
            "SELECT e.*, p.id as position_id, p.name as position_name, p.description as position_description " +
                    "FROM employees e JOIN employee_positions p ON e.position_id = p.id";
    private static final String FIND_BY_POSITION =
            "SELECT e.*, p.id as position_id, p.name as position_name, p.description as position_description " +
                    "FROM employees e JOIN employee_positions p ON e.position_id = p.id WHERE p.name = ?";
    private static final String INSERT =
            "INSERT INTO employees (first_name, last_name, middle_name, position_id, hire_date, " +
                    "experience, passport, contact_info, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE employees SET first_name = ?, last_name = ?, middle_name = ?, position_id = ?, " +
                    "hire_date = ?, experience = ?, passport = ?, contact_info = ?, active = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM employees WHERE id = ?";
    private static final String FIND_BY_CREW_ID =
            "SELECT e.*, p.id as position_id, p.name as position_name, p.description as position_description " +
                    "FROM employees e " +
                    "JOIN employee_positions p ON e.position_id = p.id " +
                    "JOIN crew_members cm ON e.id = cm.employee_id " +
                    "WHERE cm.crew_id = ?";

    public EmployeeDao() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEmployee(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding employee by id: {}", id, e);
            return Optional.empty();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {

            while (resultSet.next()) {
                employees.add(mapResultSetToEmployee(resultSet));
            }
            return employees;
        } catch (SQLException e) {
            logger.error("Error finding all employees", e);
            return employees;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Employee save(Employee employee) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getMiddleName());
            statement.setLong(4, employee.getPosition().getId());

            if (employee.getHireDate() != null) {
                statement.setDate(5, Date.valueOf(employee.getHireDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            if (employee.getExperience() != null) {
                statement.setInt(6, employee.getExperience());
            } else {
                statement.setNull(6, Types.INTEGER);
            }

            statement.setString(7, employee.getPassport());
            statement.setString(8, employee.getContactInfo());
            statement.setBoolean(9, employee.isActive());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating employee failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setId(generatedKeys.getLong(1));
                    return employee;
                } else {
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving employee: {}", employee, e);
            return employee;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(Employee employee) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getMiddleName());
            statement.setLong(4, employee.getPosition().getId());

            if (employee.getHireDate() != null) {
                statement.setDate(5, Date.valueOf(employee.getHireDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            if (employee.getExperience() != null) {
                statement.setInt(6, employee.getExperience());
            } else {
                statement.setNull(6, Types.INTEGER);
            }

            statement.setString(7, employee.getPassport());
            statement.setString(8, employee.getContactInfo());
            statement.setBoolean(9, employee.isActive());
            statement.setLong(10, employee.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating employee: {}", employee, e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting employee with id: {}", id, e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public List<Employee> findByPosition(String positionName) {
        List<Employee> employees = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_POSITION)) {
            statement.setString(1, positionName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(mapResultSetToEmployee(resultSet));
                }
            }
            return employees;
        } catch (SQLException e) {
            logger.error("Error finding employees by position: {}", positionName, e);
            return employees;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public List<Employee> findByCrewId(Long crewId) {
        List<Employee> employees = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_CREW_ID)) {
            statement.setLong(1, crewId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(mapResultSetToEmployee(resultSet));
                }
            }
            return employees;
        } catch (SQLException e) {
            logger.error("Error finding employees by crew id: {}", crewId, e);
            return employees;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        EmployeePosition position = new EmployeePosition(
                resultSet.getLong("position_id"),
                resultSet.getString("position_name"),
                resultSet.getString("position_description")
        );

        return new Employee.Builder()
                .withId(resultSet.getLong("id"))
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name"))
                .withMiddleName(resultSet.getString("middle_name"))
                .withPosition(position)
                .withHireDate(resultSet.getDate("hire_date") != null ?
                        resultSet.getDate("hire_date").toLocalDate() : null)
                .withExperience(resultSet.getObject("experience") != null ?
                        resultSet.getInt("experience") : null)
                .withPassport(resultSet.getString("passport"))
                .withContactInfo(resultSet.getString("contact_info"))
                .withActive(resultSet.getBoolean("active"))
                .build();
    }

    // Дополнение к EmployeeDao.java
    private static final String FIND_ALL_POSITIONS =
            "SELECT * FROM employee_positions";
    private static final String FIND_POSITION_BY_ID =
            "SELECT * FROM employee_positions WHERE id = ?";

    public List<EmployeePosition> getAllPositions() {
        List<EmployeePosition> positions = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_POSITIONS)) {

            while (resultSet.next()) {
                positions.add(mapResultSetToPosition(resultSet));
            }
            return positions;
        } catch (SQLException e) {
            logger.error("Error finding all positions", e);
            return positions;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public Optional<EmployeePosition> getPositionById(Long id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_POSITION_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToPosition(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding position by id: {}", id, e);
            return Optional.empty();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private EmployeePosition mapResultSetToPosition(ResultSet resultSet) throws SQLException {
        return new EmployeePosition(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description")
        );
    }

}