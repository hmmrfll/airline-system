// FlightEditCommand.java
package com.airline.controller.command.flight;

import com.airline.controller.command.Command;
import com.airline.model.entity.Flight;
import com.airline.model.service.FlightService;
import com.airline.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class FlightEditCommand implements Command {
    private static final Logger logger = LogManager.getLogger(FlightEditCommand.class);
    private static final String FLIGHT_FORM_PAGE = "/WEB-INF/jsp/admin/flight-form.jsp";

    private final FlightService flightService;

    public FlightEditCommand() {
        this.flightService = new FlightService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long flightId;
        try {
            flightId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            logger.error("Invalid flight ID", e);
            request.setAttribute("error", "Неверный ID рейса");
            response.sendRedirect(request.getContextPath() + "/app?command=flightList");
            return null;
        }

        // GET-запрос - показываем форму для редактирования
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            Optional<Flight> flightOptional = flightService.findById(flightId);

            if (flightOptional.isEmpty()) {
                logger.warn("Flight with ID {} not found", flightId);
                request.setAttribute("error", "Рейс не найден");
                response.sendRedirect(request.getContextPath() + "/app?command=flightList");
                return null;
            }

            Flight flight = flightOptional.get();
            request.setAttribute("flight", flight);
            request.setAttribute("active", "flights");
            request.setAttribute("formAction", "edit");
            return FLIGHT_FORM_PAGE;
        }

        // POST-запрос - обрабатываем форму
        try {
            Optional<Flight> flightOptional = flightService.findById(flightId);

            if (flightOptional.isEmpty()) {
                logger.warn("Flight with ID {} not found for update", flightId);
                request.setAttribute("error", "Рейс не найден");
                response.sendRedirect(request.getContextPath() + "/app?command=flightList");
                return null;
            }

            Flight flight = flightOptional.get();

            // Получаем данные из формы
            String flightNumber = request.getParameter("flightNumber");
            String departureAirport = request.getParameter("departureAirport");
            String arrivalAirport = request.getParameter("arrivalAirport");
            String aircraftType = request.getParameter("aircraftType");
            String status = request.getParameter("status");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            LocalDateTime departureTime;
            LocalDateTime arrivalTime;

            try {
                departureTime = LocalDateTime.parse(request.getParameter("departureTime"), formatter);
                arrivalTime = LocalDateTime.parse(request.getParameter("arrivalTime"), formatter);
            } catch (DateTimeParseException e) {
                logger.error("Invalid date format", e);
                request.setAttribute("error", "Неверный формат даты");
                request.setAttribute("flight", flight);
                request.setAttribute("active", "flights");
                request.setAttribute("formAction", "edit");
                return FLIGHT_FORM_PAGE;
            }

            // Валидация данных
            if (!ValidationUtil.isValidFlightNumber(flightNumber) ||
                    !ValidationUtil.isStringNotEmpty(departureAirport) ||
                    !ValidationUtil.isStringNotEmpty(arrivalAirport) ||
                    !ValidationUtil.isStringNotEmpty(aircraftType) ||
                    !ValidationUtil.isValidFlightTimes(departureTime, arrivalTime)) {

                request.setAttribute("error", "Проверьте правильность введенных данных");
                request.setAttribute("flight", flight);
                request.setAttribute("active", "flights");
                request.setAttribute("formAction", "edit");
                return FLIGHT_FORM_PAGE;
            }

            // Обновляем объект рейса
            flight.setFlightNumber(flightNumber);
            flight.setDepartureAirport(departureAirport);
            flight.setArrivalAirport(arrivalAirport);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            flight.setAircraftType(aircraftType);
            flight.setStatus(status);

            // Сохраняем изменения
            boolean updated = flightService.update(flight);

            if (updated) {
                logger.info("Updated flight with ID: {}", flight.getId());
                response.sendRedirect(request.getContextPath() + "/app?command=flightList");
                return null;
            } else {
                logger.error("Failed to update flight with ID: {}", flight.getId());
                request.setAttribute("error", "Ошибка при обновлении рейса");
                request.setAttribute("flight", flight);
                request.setAttribute("active", "flights");
                request.setAttribute("formAction", "edit");
                return FLIGHT_FORM_PAGE;
            }
        } catch (Exception e) {
            logger.error("Error updating flight", e);
            request.setAttribute("error", "Ошибка при обновлении рейса: " + e.getMessage());
            request.setAttribute("active", "flights");
            request.setAttribute("formAction", "edit");
            return FLIGHT_FORM_PAGE;
        }
    }
}