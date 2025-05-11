// CrewDeleteCommand.java
package com.airline.controller.command.crew;

import com.airline.controller.command.Command;
import com.airline.model.service.CrewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrewDeleteCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CrewDeleteCommand.class);

    private final CrewService crewService;

    public CrewDeleteCommand() {
        this.crewService = new CrewService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long crewId = Long.parseLong(request.getParameter("id"));
            boolean deleted = crewService.delete(crewId);

            if (deleted) {
                logger.info("Deleted crew with ID: {}", crewId);
                request.setAttribute("message", "Бригада успешно удалена");
            } else {
                logger.warn("Failed to delete crew with ID: {}", crewId);
                request.setAttribute("error", "Невозможно удалить бригаду");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid crew ID for deletion", e);
            request.setAttribute("error", "Неверный ID бригады");
        }

        response.sendRedirect(request.getContextPath() + "/app?command=crewList");
        return null;
    }
}