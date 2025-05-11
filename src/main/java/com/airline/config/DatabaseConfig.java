package com.airline.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class);
    private static final Properties properties = new Properties();
    private static DatabaseConfig instance;

    private DatabaseConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(inputStream);
            logger.info("Database configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading database configuration", e);
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getUrl() {
        return getProperty("db.url");
    }

    public String getUsername() {
        return getProperty("db.username");
    }

    public String getPassword() {
        return getProperty("db.password");
    }

    public String getDriver() {
        return getProperty("db.driver");
    }

    public int getPoolSize() {
        return Integer.parseInt(getProperty("db.pool.size"));
    }

    public boolean testConnection() {
        try (Connection connection = DriverManager.getConnection(
                getUrl(), getUsername(), getPassword())) {
            return connection.isValid(1);
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
}