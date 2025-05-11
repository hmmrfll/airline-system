// FlightListCommand.java
package com.airline.controller.command.flight;

import com.airline.controller.command.Command;
import com.airline.model.entity.Flight;
import com.airline.model.service.FlightService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FlightListCommand implements Command {
    private static final Logger logger = LogManager.getLogger(FlightListCommand.class);
    private static final String FLIGHTS_PAGE = "/WEB-INF/jsp/admin/flights.jsp";

    private final FlightService flightService;

    public FlightListCommand() {
        this.flightService = new FlightService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Flight> flights = flightService.findAll();
        request.setAttribute("flights", flights);
        request.setAttribute("active", "flights");

        logger.info("Retrieved {} flights for display", flights.size());
        return FLIGHTS_PAGE;
    }
}