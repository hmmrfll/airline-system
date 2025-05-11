package com.airline.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private final BlockingQueue<Connection> availableConnections;
    private final List<Connection> usedConnections = new ArrayList<>();
    private final int MAX_POOL_SIZE;

    private ConnectionPool() {
        DatabaseConfig config = DatabaseConfig.getInstance();

        try {
            Class.forName(config.getDriver());
            logger.info("JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Cannot load JDBC driver", e);
            throw new RuntimeException("Cannot load JDBC driver", e);
        }

        MAX_POOL_SIZE = config.getPoolSize();
        availableConnections = new LinkedBlockingQueue<>(MAX_POOL_SIZE);

        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            try {
                Connection connection = createConnection();
                availableConnections.add(connection);
            } catch (SQLException e) {
                logger.error("Error creating connection for pool", e);
            }
        }

        if (availableConnections.isEmpty()) {
            logger.fatal("Failed to initialize connection pool");
            throw new RuntimeException("Failed to initialize connection pool");
        }

        logger.info("Connection pool initialized with {} connections", availableConnections.size());
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private Connection createConnection() throws SQLException {
        DatabaseConfig config = DatabaseConfig.getInstance();
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }

    public synchronized Connection getConnection() {
        try {
            Connection connection = availableConnections.take();
            usedConnections.add(connection);
            logger.debug("Connection obtained from pool. Available: {}, Used: {}",
                    availableConnections.size(), usedConnections.size());
            return connection;
        } catch (InterruptedException e) {
            logger.error("Thread interrupted while waiting for connection", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Cannot get connection", e);
        }
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            try {
                // Проверяем, не закрыто ли соединение
                if (!connection.isClosed()) {
                    if (connection.isValid(1)) {
                        // Сбрасываем autocommit в true для следующего использования
                        if (!connection.getAutoCommit()) {
                            connection.setAutoCommit(true);
                        }
                        availableConnections.add(connection);
                        logger.debug("Connection returned to pool. Available: {}, Used: {}",
                                availableConnections.size(), usedConnections.size());
                    } else {
                        // Если соединение не валидно, создаем новое
                        logger.warn("Invalid connection detected, creating new connection");
                        availableConnections.add(createConnection());
                        closeConnection(connection);
                    }
                } else {
                    // Если соединение закрыто, создаем новое
                    logger.warn("Closed connection detected, creating new connection");
                    availableConnections.add(createConnection());
                }
            } catch (SQLException e) {
                logger.error("Error releasing connection", e);
                try {
                    availableConnections.add(createConnection());
                    closeConnection(connection);
                } catch (SQLException ex) {
                    logger.error("Error creating new connection", ex);
                }
            }
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing connection", e);
        }
    }

    public synchronized void shutdown() {
        for (Connection connection : usedConnections) {
            closeConnection(connection);
        }
        usedConnections.clear();

        for (Connection connection : availableConnections) {
            closeConnection(connection);
        }
        availableConnections.clear();

        logger.info("Connection pool shut down");
    }
}