package com.airline.model.dao;

import com.airline.config.ConnectionPool;
import com.airline.model.entity.Flight;
import com.airline.model.entity.Role;
import com.airline.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements GenericDao<Flight, Long> {
    private static final Logger logger = LogManager.getLogger(FlightDao.class);
    private final ConnectionPool connectionPool;

    // SQL запросы
    private static final String FIND_BY_ID =
            "SELECT f.*, u.id as user_id, u.username, u.first_name, u.last_name, " +
                    "r.id as role_id, r.name as role_name, r.description as role_description " +
                    "FROM flights f " +
                    "LEFT JOIN users u ON f.created_by = u.id " +
                    "LEFT JOIN roles r ON u.role_id = r.id " +
                    "WHERE f.id = ?";
    private static final String FIND_ALL =
            "SELECT f.*, u.id as user_id, u.username, u.first_name, u.last_name, " +
                    "r.id as role_id, r.name as role_name, r.description as role_description " +
                    "FROM flights f " +
                    "LEFT JOIN users u ON f.created_by = u.id " +
                    "LEFT JOIN roles r ON u.role_id = r.id";
    private static final String INSERT =
            "INSERT INTO flights (flight_number, departure_airport, arrival_airport, " +
                    "departure_time, arrival_time, aircraft_type, status, created_by, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE flights SET flight_number = ?, departure_airport = ?, arrival_airport = ?, " +
                    "departure_time = ?, arrival_time = ?, aircraft_type = ?, status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM flights WHERE id = ?";

    public FlightDao() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<Flight> findById(Long id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToFlight(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding flight by id: {}", id, e);
            return Optional.empty();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {

            while (resultSet.next()) {
                flights.add(mapResultSetToFlight(resultSet));
            }
            return flights;
        } catch (SQLException e) {
            logger.error("Error finding all flights", e);
            return flights;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public Flight save(Flight flight) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, flight.getFlightNumber());
            statement.setString(2, flight.getDepartureAirport());
            statement.setString(3, flight.getArrivalAirport());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            statement.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            statement.setString(6, flight.getAircraftType());
            statement.setString(7, flight.getStatus());

            if (flight.getCreatedBy() != null) {
                statement.setLong(8, flight.getCreatedBy().getId());
            } else {
                statement.setNull(8, Types.BIGINT);
            }

            LocalDateTime createdAt = flight.getCreatedAt() != null ?
                    flight.getCreatedAt() : LocalDateTime.now();
            statement.setTimestamp(9, Timestamp.valueOf(createdAt));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating flight failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    flight.setId(generatedKeys.getLong(1));
                    return flight;
                } else {
                    throw new SQLException("Creating flight failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving flight: {}", flight, e);
            return flight;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(Flight flight) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setString(1, flight.getFlightNumber());
            statement.setString(2, flight.getDepartureAirport());
            statement.setString(3, flight.getArrivalAirport());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            statement.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            statement.setString(6, flight.getAircraftType());
            statement.setString(7, flight.getStatus());
            statement.setLong(8, flight.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating flight: {}", flight, e);
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
            logger.error("Error deleting flight with id: {}", id, e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private Flight mapResultSetToFlight(ResultSet resultSet) throws SQLException {
        Long userId = resultSet.getObject("user_id") != null ? resultSet.getLong("user_id") : null;
        User createdBy = null;

        if (userId != null) {
            Role role = new Role(
                    resultSet.getLong("role_id"),
                    resultSet.getString("role_name"),
                    resultSet.getString("role_description")
            );

            createdBy = new User.Builder()
                    .withId(userId)
                    .withUsername(resultSet.getString("username"))
                    .withFirstName(resultSet.getString("first_name"))
                    .withLastName(resultSet.getString("last_name"))
                    .withRole(role)
                    .build();
        }

        return new Flight.Builder()
                .withId(resultSet.getLong("id"))
                .withFlightNumber(resultSet.getString("flight_number"))
                .withDepartureAirport(resultSet.getString("departure_airport"))
                .withArrivalAirport(resultSet.getString("arrival_airport"))
                .withDepartureTime(resultSet.getTimestamp("departure_time").toLocalDateTime())
                .withArrivalTime(resultSet.getTimestamp("arrival_time").toLocalDateTime())
                .withAircraftType(resultSet.getString("aircraft_type"))
                .withStatus(resultSet.getString("status"))
                .withCreatedBy(createdBy)
                .withCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}