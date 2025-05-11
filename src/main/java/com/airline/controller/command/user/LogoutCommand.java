// LogoutCommand.java
package com.airline.controller.command.user;

import com.airline.controller.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = session.getAttribute("user") != null ?
                    ((com.airline.model.entity.User) session.getAttribute("user")).getUsername() : "unknown";

            logger.info("User '{}' logged out", username);
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/app?command=login");
        return null;
    }
}