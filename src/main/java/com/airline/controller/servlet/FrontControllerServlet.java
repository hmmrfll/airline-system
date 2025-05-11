// FrontControllerServlet.java
package com.airline.controller.servlet;

import com.airline.config.DatabaseInitializer;
import com.airline.controller.command.Command;
import com.airline.controller.command.CommandFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/app/*")
public class FrontControllerServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(FrontControllerServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();

        // Инициализируем базу данных при старте сервлета
        DatabaseInitializer initializer = new DatabaseInitializer();
        try {
            initializer.initDatabase();
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing database", e);
            throw new ServletException("Error initializing database", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String commandName = request.getParameter("command");
            if (commandName == null || commandName.isEmpty()) {
                commandName = "login"; // По умолчанию переходим на страницу входа
            }

            logger.debug("Processing command: {}", commandName);

            Command command = CommandFactory.getInstance().getCommand(commandName);
            String page = command.execute(request, response);

            if (page != null) {
                logger.debug("Forwarding to page: {}", page);
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage(), e);
            request.setAttribute("errorMessage", "Произошла ошибка при обработке запроса: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error/500.jsp").forward(request, response);
        }
    }
}