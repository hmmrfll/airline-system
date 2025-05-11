package com.airline.controller.command.user;

import com.airline.controller.command.Command;
import com.airline.model.entity.Role;
import com.airline.model.entity.User;
import com.airline.model.service.UserService;
import com.airline.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);
    private static final String REGISTER_PAGE = "/WEB-INF/jsp/register.jsp";
    private static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";

    private final UserService userService;

    public RegisterCommand() {
        this.userService = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Если это GET-запрос, показываем страницу регистрации
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return REGISTER_PAGE;
        }

        // Если это POST-запрос, обрабатываем форму регистрации
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // Проверяем, что все поля заполнены
        if (username == null || password == null || confirmPassword == null ||
                firstName == null || lastName == null || email == null ||
                username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {

            request.setAttribute("error", "Все поля обязательны для заполнения");
            return REGISTER_PAGE;
        }

        // Проверяем, что пароли совпадают
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Пароли не совпадают");
            return REGISTER_PAGE;
        }

        // Проверяем, что email валидный
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Неверный формат email");
            return REGISTER_PAGE;
        }

        // Проверяем, что пользователь с таким username не существует
        Optional<User> existingUser = userService.findByUsername(username);
        if (existingUser.isPresent()) {
            request.setAttribute("error", "Пользователь с таким именем уже существует");
            return REGISTER_PAGE;
        }

        try {
            // Получаем роль DISPATCHER (по умолчанию для регистрации)
            Role dispatcherRole = new Role(2L, "DISPATCHER", "Диспетчер рейсов");

            // Создаем нового пользователя
            User newUser = new User.Builder()
                    .withUsername(username)
                    .withPassword(password) // В реальной системе здесь должно быть хеширование
                    .withFirstName(firstName)
                    .withLastName(lastName)
                    .withEmail(email)
                    .withRole(dispatcherRole)
                    .withCreatedAt(LocalDateTime.now())
                    .withActive(true)
                    .build();

            // Сохраняем пользователя
            User savedUser = userService.save(newUser);
            logger.info("New user registered: {}", username);

            // Перенаправляем на страницу входа с сообщением об успешной регистрации
            request.setAttribute("success", "Регистрация успешно завершена. Теперь вы можете войти в систему.");
            return LOGIN_PAGE;
        } catch (Exception e) {
            logger.error("Error registering new user", e);
            request.setAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return REGISTER_PAGE;
        }
    }
}