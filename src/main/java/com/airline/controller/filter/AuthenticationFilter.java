// AuthenticationFilter.java
package com.airline.controller.filter;

import com.airline.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/app/*"})
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
    private static final String LOGIN_COMMAND = "login";
    private static final String LOGOUT_COMMAND = "logout";
    private static final String REGISTER_COMMAND = "register";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Authentication filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String command = httpRequest.getParameter("command");
        boolean isLoginRequest = LOGIN_COMMAND.equals(command);
        boolean isLogoutRequest = LOGOUT_COMMAND.equals(command);
        boolean isLoggedIn = session != null && session.getAttribute("user") != null;
        boolean isRegisterRequest = REGISTER_COMMAND.equals(command);

        // Добавить проверку для index.jsp
        String requestURI = httpRequest.getRequestURI();
        boolean isIndexPage = requestURI.endsWith("index.jsp");

        if (isLoggedIn || isLoginRequest || isLogoutRequest || isRegisterRequest || isIndexPage) {
            // Проверка доступа по ролям может быть добавлена здесь
            if (isLoggedIn) {
                User user = (User) session.getAttribute("user");
                String role = user.getRole().getName();
                logger.debug("User {} with role {} accessing command: {}",
                        user.getUsername(), role, command);

                // Проверяем права доступа только если указана команда
                if (command != null && !hasAccess(role, command)) {
                    logger.warn("Access denied for user {} to command {}", user.getUsername(), command);
                    httpResponse.sendRedirect(httpRequest.getContextPath() +
                            "/app?command=login&error=access_denied");
                    return;
                }
            }

            chain.doFilter(request, response);
        } else {
            logger.warn("Unauthenticated access attempt to protected resource: {}", httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/app?command=login");
        }
    }

    @Override
    public void destroy() {
        logger.info("Authentication filter destroyed");
    }

    private boolean hasAccess(String role, String command) {
        // Упрощенная логика проверки доступа
        // В реальном приложении здесь будет более сложная логика
        if (command == null) {
            return true;
        }

        if ("ADMIN".equals(role)) {
            // Администратор имеет доступ ко всем командам
            return true;
        } else if ("DISPATCHER".equals(role)) {
            // Диспетчер имеет доступ только к определенным командам
            return !command.startsWith("flight") || // Нет доступа к управлению рейсами
                    command.equals("flightList"); // Но может просматривать список рейсов
        }

        return false;
    }
}