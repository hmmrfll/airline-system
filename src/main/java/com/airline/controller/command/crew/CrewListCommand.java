// CrewListCommand.java
package com.airline.controller.command.crew;

import com.airline.controller.command.Command;
import com.airline.model.entity.Crew;
import com.airline.model.service.CrewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CrewListCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CrewListCommand.class);
    private static final String CREWS_PAGE = "/WEB-INF/jsp/dispatcher/crews.jsp";

    private final CrewService crewService;

    public CrewListCommand() {
        this.crewService = new CrewService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Crew> crews = crewService.findAll();
        request.setAttribute("crews", crews);
        request.setAttribute("active", "crews");

        logger.info("Retrieved {} crews for display", crews.size());
        return CREWS_PAGE;
    }
}