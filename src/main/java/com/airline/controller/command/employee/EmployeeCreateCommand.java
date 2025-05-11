// EmployeeCreateCommand.java
package com.airline.controller.command.employee;

import com.airline.controller.command.Command;
import com.airline.model.entity.Employee;
import com.airline.model.entity.EmployeePosition;
import com.airline.model.service.EmployeeService;
import com.airline.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmployeeCreateCommand implements Command {
    private static final Logger logger = LogManager.getLogger(EmployeeCreateCommand.class);
    private static final String EMPLOYEE_FORM_PAGE = "/WEB-INF/jsp/admin/employee-form.jsp";

    private final EmployeeService employeeService;

    public EmployeeCreateCommand() {
        this.employeeService = new EmployeeService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // GET-запрос - показываем форму для создания
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("active", "employees");
            request.setAttribute("formAction", "create");

            // Получаем список возможных должностей
            List<EmployeePosition> positions = employeeService.getAllPositions();
            request.setAttribute("positions", positions);

            return EMPLOYEE_FORM_PAGE;
        }

        // POST-запрос - обрабатываем форму
        try {
            // Получаем данные из формы
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String middleName = request.getParameter("middleName");
            Long positionId = Long.parseLong(request.getParameter("positionId"));
            String passport = request.getParameter("passport");
            String contactInfo = request.getParameter("contactInfo");
            boolean active = "on".equals(request.getParameter("active"));

            Integer experience = null;
            if (request.getParameter("experience") != null && !request.getParameter("experience").isEmpty()) {
                experience = Integer.parseInt(request.getParameter("experience"));
            }

            LocalDate hireDate = null;
            try {
                if (request.getParameter("hireDate") != null && !request.getParameter("hireDate").isEmpty()) {
                    hireDate = LocalDate.parse(request.getParameter("hireDate"), DateTimeFormatter.ISO_DATE);
                }
            } catch (DateTimeParseException e) {
                logger.error("Invalid date format", e);
                request.setAttribute("error", "Неверный формат даты");
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "create");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            // Получаем позицию
            EmployeePosition position = employeeService.getPositionById(positionId).orElse(null);

            if (position == null) {
                request.setAttribute("error", "Выбрана несуществующая должность");
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "create");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            // Валидация данных
            if (!ValidationUtil.isStringNotEmpty(firstName) ||
                    !ValidationUtil.isStringNotEmpty(lastName)) {

                request.setAttribute("error", "Имя и фамилия обязательны");
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "create");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            if (passport != null && !passport.isEmpty() && !ValidationUtil.isValidPassport(passport)) {
                request.setAttribute("error", "Неверный формат паспортных данных");
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "create");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            // Создаем объект сотрудника
            Employee employee = new Employee.Builder()
                    .withFirstName(firstName)
                    .withLastName(lastName)
                    .withMiddleName(middleName)
                    .withPosition(position)
                    .withHireDate(hireDate)
                    .withExperience(experience)
                    .withPassport(passport)
                    .withContactInfo(contactInfo)
                    .withActive(active)
                    .build();

            // Сохраняем сотрудника
            Employee savedEmployee = employeeService.save(employee);
            logger.info("Created new employee with ID: {}", savedEmployee.getId());

            // Перенаправляем на список сотрудников
            response.sendRedirect(request.getContextPath() + "/app?command=employeeList");
            return null;
        } catch (Exception e) {
            logger.error("Error creating employee", e);
            request.setAttribute("error", "Ошибка при создании сотрудника: " + e.getMessage());
            request.setAttribute("active", "employees");
            request.setAttribute("formAction", "create");
            List<EmployeePosition> positions = employeeService.getAllPositions();
            request.setAttribute("positions", positions);
            return EMPLOYEE_FORM_PAGE;
        }
    }
}