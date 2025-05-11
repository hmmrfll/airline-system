// FlightDeleteCommand.java
package com.airline.controller.command.flight;

import com.airline.controller.command.Command;
import com.airline.model.service.FlightService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FlightDeleteCommand implements Command {
    private static final Logger logger = LogManager.getLogger(FlightDeleteCommand.class);

    private final FlightService flightService;

    public FlightDeleteCommand() {
        this.flightService = new FlightService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long flightId = Long.parseLong(request.getParameter("id"));
            boolean deleted = flightService.delete(flightId);

            if (deleted) {
                logger.info("Deleted flight with ID: {}", flightId);
                request.setAttribute("message", "Рейс успешно удален");
            } else {
                logger.warn("Failed to delete flight with ID: {}", flightId);
                request.setAttribute("error", "Невозможно удалить рейс");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid flight ID for deletion", e);
            request.setAttribute("error", "Неверный ID рейса");
        }

        response.sendRedirect(request.getContextPath() + "/app?command=flightList");
        return null;
    }
}