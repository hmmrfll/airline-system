// FlightCreateCommand.java
package com.airline.controller.command.flight;

import com.airline.controller.command.Command;
import com.airline.model.entity.Flight;
import com.airline.model.entity.User;
import com.airline.model.service.FlightService;
import com.airline.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FlightCreateCommand implements Command {
    private static final Logger logger = LogManager.getLogger(FlightCreateCommand.class);
    private static final String FLIGHT_FORM_PAGE = "/WEB-INF/jsp/admin/flight-form.jsp";
    private static final String REDIRECT_TO_FLIGHTS = "redirect:/app?command=flightList";

    private final FlightService flightService;

    public FlightCreateCommand() {
        this.flightService = new FlightService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // GET-запрос - показываем форму для создания
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("active", "flights");
            request.setAttribute("formAction", "create");
            return FLIGHT_FORM_PAGE;
        }

        // POST-запрос - обрабатываем форму
        try {
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
                request.setAttribute("active", "flights");
                request.setAttribute("formAction", "create");
                return FLIGHT_FORM_PAGE;
            }

            // Валидация данных
            if (!ValidationUtil.isValidFlightNumber(flightNumber) ||
                    !ValidationUtil.isStringNotEmpty(departureAirport) ||
                    !ValidationUtil.isStringNotEmpty(arrivalAirport) ||
                    !ValidationUtil.isStringNotEmpty(aircraftType) ||
                    !ValidationUtil.isValidFlightTimes(departureTime, arrivalTime)) {

                request.setAttribute("error", "Проверьте правильность введенных данных");
                request.setAttribute("active", "flights");
                request.setAttribute("formAction", "create");
                return FLIGHT_FORM_PAGE;
            }

            // Получаем текущего пользователя из сессии
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            // Создаем объект рейса
            Flight flight = new Flight.Builder()
                    .withFlightNumber(flightNumber)
                    .withDepartureAirport(departureAirport)
                    .withArrivalAirport(arrivalAirport)
                    .withDepartureTime(departureTime)
                    .withArrivalTime(arrivalTime)
                    .withAircraftType(aircraftType)
                    .withStatus(status)
                    .withCreatedBy(currentUser)
                    .withCreatedAt(LocalDateTime.now())
                    .build();

            // Сохраняем рейс
            Flight savedFlight = flightService.save(flight);
            logger.info("Created new flight with ID: {}", savedFlight.getId());

            // Перенаправляем на список рейсов
            response.sendRedirect(request.getContextPath() + "/app?command=flightList");
            return null;
        } catch (Exception e) {
            logger.error("Error creating flight", e);
            request.setAttribute("error", "Ошибка при создании рейса: " + e.getMessage());
            request.setAttribute("active", "flights");
            request.setAttribute("formAction", "create");
            return FLIGHT_FORM_PAGE;
        }
    }
}