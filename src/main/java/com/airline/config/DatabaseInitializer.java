package com.airline.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private static final Logger logger = LogManager.getLogger(DatabaseInitializer.class);
    private static final String[] REQUIRED_TABLES = {
            "roles", "users", "employee_positions", "employees",
            "flights", "crews", "crew_members"
    };

    private final ConnectionPool connectionPool;

    public DatabaseInitializer() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void initDatabase() {
        Connection connection = null;

        try {
            connection = connectionPool.getConnection();
            List<String> missingTables = checkTables(connection);

            if (!missingTables.isEmpty()) {
                logger.info("Missing tables detected: {}", missingTables);
                executeInitScript(connection);
                logger.info("Database initialization completed successfully");
            } else {
                logger.info("All required tables exist. Database is ready.");
            }
        } catch (SQLException | IOException e) {
            logger.error("Error initializing database", e);
            throw new RuntimeException("Error initializing database", e);
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }

    private List<String> checkTables(Connection connection) throws SQLException {
        List<String> missingTables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        for (String tableName : REQUIRED_TABLES) {
            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            if (!tables.next()) {
                missingTables.add(tableName);
            }
            tables.close();
        }

        return missingTables;
    }

    private void executeInitScript(Connection connection) throws SQLException, IOException {
        String sqlScript = loadInitScript();
        String[] statements = sqlScript.split(";");

        try (Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                if (!sql.trim().isEmpty()) {
                    statement.execute(sql);
                }
            }
        }
    }

    private String loadInitScript() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("init_database.sql")) {
            if (is == null) {
                throw new IOException("Cannot find init_database.sql in resources");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}