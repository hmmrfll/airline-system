// LoginCommand.java
package com.airline.controller.command.user;

import com.airline.controller.command.Command;
import com.airline.model.entity.User;
import com.airline.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";
    private static final String ADMIN_HOME = "/WEB-INF/jsp/admin/home.jsp";
    private static final String DISPATCHER_HOME = "/WEB-INF/jsp/dispatcher/home.jsp";

    private final UserService userService;

    public LoginCommand() {
        this.userService = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Если это GET-запрос, просто показываем страницу логина
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return LOGIN_PAGE;
        }

        // Если это POST-запрос, обрабатываем ввод пользователя
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            logger.warn("Login attempt with empty username or password");
            request.setAttribute("error", "Имя пользователя и пароль обязательны");
            return LOGIN_PAGE;
        }

        Optional<User> userOptional = userService.authenticate(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User successfully authenticated: {}", username);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().getName());

            // В зависимости от роли, перенаправляем на соответствующую страницу
            if ("ADMIN".equals(user.getRole().getName())) {
                return ADMIN_HOME;
            } else {
                return DISPATCHER_HOME;
            }
        } else {
            logger.warn("Failed login attempt for username: {}", username);
            request.setAttribute("error", "Неверное имя пользователя или пароль");
            return LOGIN_PAGE;
        }
    }
}