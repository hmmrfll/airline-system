// CrewEditCommand.java
package com.airline.controller.command.crew;

import com.airline.controller.command.Command;
import com.airline.model.entity.Crew;
import com.airline.model.entity.Employee;
import com.airline.model.entity.Flight;
import com.airline.model.service.CrewService;
import com.airline.model.service.FlightService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CrewEditCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CrewEditCommand.class);
    private static final String CREW_FORM_PAGE = "/WEB-INF/jsp/dispatcher/crew-form.jsp";
    private static final String CREW_MEMBERS_PAGE = "/WEB-INF/jsp/dispatcher/crew-members.jsp";

    private final CrewService crewService;
    private final FlightService flightService;

    public CrewEditCommand() {
        this.crewService = new CrewService();
        this.flightService = new FlightService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long crewId;
        try {
            crewId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            logger.error("Invalid crew ID", e);
            request.setAttribute("error", "Неверный ID бригады");
            response.sendRedirect(request.getContextPath() + "/app?command=crewList");
            return null;
        }

        // Находим бригаду
        Optional<Crew> crewOptional = crewService.findById(crewId);
        if (crewOptional.isEmpty()) {
            logger.warn("Crew with ID {} not found", crewId);
            request.setAttribute("error", "Бригада не найдена");
            response.sendRedirect(request.getContextPath() + "/app?command=crewList");
            return null;
        }

        Crew crew = crewOptional.get();

        // GET-запрос - проверяем, нужна ли форма редактирования или форма управления составом бригады
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // Если параметр membersMode == true, то показываем форму управления составом
            String membersMode = request.getParameter("membersMode");
            if ("true".equals(membersMode)) {
                request.setAttribute("crew", crew);
                request.setAttribute("active", "crews");

                // Получаем список всех доступных сотрудников для добавления в бригаду
                List<Employee> availableEmployees = crewService.getAvailableEmployees(crewId);
                request.setAttribute("availableEmployees", availableEmployees);

                return CREW_MEMBERS_PAGE;
            } else {
                // Иначе показываем форму редактирования бригады
                request.setAttribute("crew", crew);
                request.setAttribute("active", "crews");
                request.setAttribute("formAction", "edit");

                // Получаем список доступных рейсов
                List<Flight> flights = flightService.findUpcomingFlights();
                request.setAttribute("flights", flights);

                return CREW_FORM_PAGE;
            }
        }

        // POST-запрос - обрабатываем форму редактирования
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
                    request.setAttribute("crew", crew);
                    request.setAttribute("active", "crews");
                    request.setAttribute("formAction", "edit");
                    List<Flight> flights = flightService.findUpcomingFlights();
                    request.setAttribute("flights", flights);
                    return CREW_FORM_PAGE;
                }
            }

            // Обновляем бригаду
            crew.setName(name);
            crew.setFlight(flight);

            // Сохраняем изменения
            boolean updated = crewService.update(crew);

            if (updated) {
                logger.info("Updated crew with ID: {}", crew.getId());

                // После обновления основных данных переходим к управлению составом
                response.sendRedirect(request.getContextPath() + "/app?command=crewEdit&id=" + crew.getId() + "&membersMode=true");
                return null;
            } else {
                logger.error("Failed to update crew with ID: {}", crew.getId());
                request.setAttribute("error", "Ошибка при обновлении бригады");
                request.setAttribute("crew", crew);
                request.setAttribute("active", "crews");
                request.setAttribute("formAction", "edit");
                List<Flight> flights = flightService.findUpcomingFlights();
                request.setAttribute("flights", flights);
                return CREW_FORM_PAGE;
            }
        } catch (Exception e) {
            logger.error("Error updating crew", e);
            request.setAttribute("error", "Ошибка при обновлении бригады: " + e.getMessage());
            request.setAttribute("crew", crew);
            request.setAttribute("active", "crews");
            request.setAttribute("formAction", "edit");
            List<Flight> flights = flightService.findUpcomingFlights();
            request.setAttribute("flights", flights);
            return CREW_FORM_PAGE;
        }
    }
}