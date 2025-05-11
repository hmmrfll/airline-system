// CrewRemoveMemberCommand.java
package com.airline.controller.command.crew;

import com.airline.controller.command.Command;
import com.airline.model.service.CrewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrewRemoveMemberCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CrewRemoveMemberCommand.class);

    private final CrewService crewService;

    public CrewRemoveMemberCommand() {
        this.crewService = new CrewService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long crewId = Long.parseLong(request.getParameter("crewId"));
            Long employeeId = Long.parseLong(request.getParameter("employeeId"));

            boolean removed = crewService.removeMember(crewId, employeeId);

            if (removed) {
                logger.info("Removed employee {} from crew {}", employeeId, crewId);
                request.setAttribute("message", "Сотрудник успешно удален из бригады");
            } else {
                logger.warn("Failed to remove employee {} from crew {}", employeeId, crewId);
                request.setAttribute("error", "Невозможно удалить сотрудника из бригады");
            }

            // Возвращаемся на страницу управления составом бригады
            response.sendRedirect(request.getContextPath() + "/app?command=crewEdit&id=" + crewId + "&membersMode=true");
            return null;
        } catch (NumberFormatException e) {
            logger.error("Invalid crew or employee ID", e);
            request.setAttribute("error", "Неверный ID бригады или сотрудника");
            response.sendRedirect(request.getContextPath() + "/app?command=crewList");
            return null;
        }
    }
}