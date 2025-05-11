// CrewDao.java
package com.airline.model.dao;

import com.airline.config.ConnectionPool;
import com.airline.model.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrewDao implements GenericDao<Crew, Long> {
    private static final Logger logger = LogManager.getLogger(CrewDao.class);
    private final ConnectionPool connectionPool;
    private final FlightDao flightDao;
    private final UserDao userDao;

    // SQL запросы
    private static final String FIND_BY_ID =
            "SELECT c.*, f.id as flight_id, u.id as user_id " +
                    "FROM crews c " +
                    "LEFT JOIN flights f ON c.flight_id = f.id " +
                    "LEFT JOIN users u ON c.created_by = u.id " +
                    "WHERE c.id = ?";
    private static final String FIND_ALL =
            "SELECT c.*, f.id as flight_id, u.id as user_id " +
                    "FROM crews c " +
                    "LEFT JOIN flights f ON c.flight_id = f.id " +
                    "LEFT JOIN users u ON c.created_by = u.id";
    private static final String FIND_BY_FLIGHT_ID =
            "SELECT c.*, f.id as flight_id, u.id as user_id " +
                    "FROM crews c " +
                    "LEFT JOIN flights f ON c.flight_id = f.id " +
                    "LEFT JOIN users u ON c.created_by = u.id " +
                    "WHERE c.flight_id = ?";
    private static final String INSERT =
            "INSERT INTO crews (flight_id, name, created_by, created_at) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE crews SET flight_id = ?, name = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM crews WHERE id = ?";
    private static final String ADD_MEMBER =
            "INSERT INTO crew_members (crew_id, employee_id) VALUES (?, ?)";
    private static final String REMOVE_MEMBER =
            "DELETE FROM crew_members WHERE crew_id = ? AND employee_id = ?";
    private static final String DELETE_MEMBERS =
            "DELETE FROM crew_members WHERE crew_id = ?";

    public CrewDao() {
        this.connectionPool = ConnectionPool.getInstance();
        this.flightDao = new FlightDao();
        this.userDao = new UserDao();
    }

    @Override
    public Optional<Crew> findById(Long id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToCrew(resultSet, connection));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding crew by id: {}", id, e);
            return Optional.empty();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Crew> findAll() {
        List<Crew> crews = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {

            while (resultSet.next()) {
                crews.add(mapResultSetToCrew(resultSet, connection));
            }
            return crews;
        } catch (SQLException e) {
            logger.error("Error finding all crews", e);
            return crews;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public List<Crew> findByFlightId(Long flightId) {
        List<Crew> crews = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_FLIGHT_ID)) {
            statement.setLong(1, flightId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    crews.add(mapResultSetToCrew(resultSet, connection));
                }
            }
            return crews;
        } catch (SQLException e) {
            logger.error("Error finding crews by flight id: {}", flightId, e);
            return crews;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Crew save(Crew crew) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            if (crew.getFlight() != null) {
                statement.setLong(1, crew.getFlight().getId());
            } else {
                statement.setNull(1, Types.BIGINT);
            }

            statement.setString(2, crew.getName());

            if (crew.getCreatedBy() != null) {
                statement.setLong(3, crew.getCreatedBy().getId());
            } else {
                statement.setNull(3, Types.BIGINT);
            }

            LocalDateTime createdAt = crew.getCreatedAt() != null ?
                    crew.getCreatedAt() : LocalDateTime.now();
            statement.setTimestamp(4, Timestamp.valueOf(createdAt));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating crew failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    crew.setId(generatedKeys.getLong(1));
                    // Если есть члены бригады, добавляем их
                    if (!crew.getMembers().isEmpty()) {
                        for (Employee employee : crew.getMembers()) {
                            addMember(crew.getId(), employee.getId(), connection);
                        }
                    }
                    return crew;
                } else {
                    throw new SQLException("Creating crew failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving crew: {}", crew, e);
            return crew;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(Crew crew) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            if (crew.getFlight() != null) {
                statement.setLong(1, crew.getFlight().getId());
            } else {
                statement.setNull(1, Types.BIGINT);
            }

            statement.setString(2, crew.getName());
            statement.setLong(3, crew.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating crew: {}", crew, e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean delete(Long id) {
        Connection connection = connectionPool.getConnection();
        try {
            // Удаляем сначала связи членов бригады
            try (PreparedStatement membersStatement = connection.prepareStatement(DELETE_MEMBERS)) {
                membersStatement.setLong(1, id);
                membersStatement.executeUpdate();
            }

            // Затем удаляем саму бригаду
            try (PreparedStatement crewStatement = connection.prepareStatement(DELETE)) {
                crewStatement.setLong(1, id);
                int affectedRows = crewStatement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            logger.error("Error deleting crew with id: {}", id, e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public boolean addMember(Long crewId, Long employeeId) {
        Connection connection = connectionPool.getConnection();
        try {
            return addMember(crewId, employeeId, connection);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private boolean addMember(Long crewId, Long employeeId, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(ADD_MEMBER)) {
            statement.setLong(1, crewId);
            statement.setLong(2, employeeId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Если ошибка из-за дубликата (уникальное ограничение), просто логируем
            if (e.getSQLState().equals("23505")) { // PostgreSQL код для нарушения уникальности
                logger.warn("Employee {} already in crew {}", employeeId, crewId);
                return false;
            } else {
                logger.error("Error adding member to crew", e);
                return false;
            }
        }
    }

    public boolean removeMember(Long crewId, Long employeeId) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_MEMBER)) {
            statement.setLong(1, crewId);
            statement.setLong(2, employeeId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error removing member from crew", e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private Crew mapResultSetToCrew(ResultSet resultSet, Connection connection) throws SQLException {
        Long crewId = resultSet.getLong("id");
        Long flightId = resultSet.getObject("flight_id") != null ? resultSet.getLong("flight_id") : null;
        Long userId = resultSet.getObject("user_id") != null ? resultSet.getLong("user_id") : null;

        Flight flight = null;
        if (flightId != null) {
            Optional<Flight> flightOptional = flightDao.findById(flightId);
            flight = flightOptional.orElse(null);
        }

        User createdBy = null;
        if (userId != null) {
            Optional<User> userOptional = userDao.findById(userId);
            createdBy = userOptional.orElse(null);
        }

        // Получаем членов бригады
        EmployeeDao employeeDao = new EmployeeDao();
        List<Employee> members = employeeDao.findByCrewId(crewId);

        return new Crew.Builder()
                .withId(crewId)
                .withFlight(flight)
                .withName(resultSet.getString("name"))
                .withCreatedBy(createdBy)
                .withCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .withMembers(members)
                .build();
    }
}