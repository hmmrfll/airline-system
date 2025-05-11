// CrewCreateCommand.java
package com.airline.controller.command.crew;

import com.airline.controller.command.Command;
import com.airline.model.entity.Crew;
import com.airline.model.entity.Flight;
import com.airline.model.entity.User;
import com.airline.model.service.CrewService;
import com.airline.model.service.FlightService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CrewCreateCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CrewCreateCommand.class);
    private static final String CREW_FORM_PAGE = "/WEB-INF/jsp/dispatcher/crew-form.jsp";

    private final CrewService crewService;
    private final FlightService flightService;

    public CrewCreateCommand() {
        this.crewService = new CrewService();
        this.flightService = new FlightService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // GET-запрос - показываем форму для создания
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("active", "crews");
            request.setAttribute("formAction", "create");

            // Получаем список доступных рейсов
            List<Flight> flights = flightService.findUpcomingFlights();
            request.setAttribute("flights", flights);

            return CREW_FORM_PAGE;
        }

        // POST-запрос - обрабатываем форму
        try {
            // Получаем данные из формы
            String name = request.getParameter("name");
            Long flightId = null;

            if (request.getParameter("flightId") != null && !request.getParameter("flightId").isEmpty()) {
                flightId = Long.parseLong(request.getParameter("flightId"));
            }

            // Получаем рейс
            Flight flight = null;
            if (flightId != null) {
                Optional<Flight> flightOptional = flightService.findById(flightId);
                flight = flightOptional.orElse(null);

                if (flight == null) {
                    logger.warn("Flight with ID {} not found", flightId);
                    request.setAttribute("error", "Рейс не найден");
                    request.setAttribute("active", "crews");
                    request.setAttribute("formAction", "create");
                    List<Flight> flights = flightService.findUpcomingFlights();
                    request.setAttribute("flights", flights);
                    return CREW_FORM_PAGE;
                }
            }

            // Получаем текущего пользователя из сессии
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            // Создаем объект бригады
            Crew crew = new Crew.Builder()
                    .withName(name)
                    .withFlight(flight)
                    .withCreatedBy(currentUser)
                    .withCreatedAt(LocalDateTime.now())
                    .build();

            // Сохраняем бригаду
            Crew savedCrew = crewService.save(crew);
            logger.info("Created new crew with ID: {}", savedCrew.getId());

            // Перенаправляем на страницу добавления членов бригады
            response.sendRedirect(request.getContextPath() + "/app?command=crewEdit&id=" + savedCrew.getId());
            return null;
        } catch (Exception e) {
            logger.error("Error creating crew", e);
            request.setAttribute("error", "Ошибка при создании бригады: " + e.getMessage());
            request.setAttribute("active", "crews");
            request.setAttribute("formAction", "create");
            List<Flight> flights = flightService.findUpcomingFlights();
            request.setAttribute("flights", flights);
            return CREW_FORM_PAGE;
        }
    }
}