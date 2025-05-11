// UserDao.java
package com.airline.model.dao;

import com.airline.config.ConnectionPool;
import com.airline.model.entity.Role;
import com.airline.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements GenericDao<User, Long> {
    private static final Logger logger = LogManager.getLogger(UserDao.class);
    private final ConnectionPool connectionPool;

    // SQL-запросы
    private static final String FIND_BY_ID =
            "SELECT u.*, r.id as role_id, r.name as role_name, r.description as role_description " +
                    "FROM users u JOIN roles r ON u.role_id = r.id WHERE u.id = ?";
    private static final String FIND_ALL =
            "SELECT u.*, r.id as role_id, r.name as role_name, r.description as role_description " +
                    "FROM users u JOIN roles r ON u.role_id = r.id";
    private static final String FIND_BY_USERNAME =
            "SELECT u.*, r.id as role_id, r.name as role_name, r.description as role_description " +
                    "FROM users u JOIN roles r ON u.role_id = r.id WHERE u.username = ?";
    private static final String INSERT =
            "INSERT INTO users (username, password, first_name, last_name, email, role_id, created_at, active) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, " +
                    "email = ?, role_id = ?, active = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";

    public UserDao() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<User> findById(Long id) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding user by id: {}", id, e);
            return Optional.empty();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            logger.error("Error finding all users", e);
            return users;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public User save(User user) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setLong(6, user.getRole().getId());

            LocalDateTime createdAt = user.getCreatedAt() != null ?
                    user.getCreatedAt() : LocalDateTime.now();
            statement.setTimestamp(7, Timestamp.valueOf(createdAt));

            statement.setBoolean(8, user.isActive());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    return user;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving user: {}", user, e);
            return user;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean update(User user) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setLong(6, user.getRole().getId());
            statement.setBoolean(7, user.isActive());
            statement.setLong(8, user.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating user: {}", user, e);
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
            logger.error("Error deleting user with id: {}", id, e);
            return false;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    public Optional<User> findByUsername(String username) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Error finding user by username: {}", username, e);
            return Optional.empty();
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        Role role = new Role(
                resultSet.getLong("role_id"),
                resultSet.getString("role_name"),
                resultSet.getString("role_description")
        );

        return new User.Builder()
                .withId(resultSet.getLong("id"))
                .withUsername(resultSet.getString("username"))
                .withPassword(resultSet.getString("password"))
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name"))
                .withEmail(resultSet.getString("email"))
                .withRole(role)
                .withCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .withActive(resultSet.getBoolean("active"))
                .build();
    }
}