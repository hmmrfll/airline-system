// EmployeeEditCommand.java
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
import java.util.Optional;

public class EmployeeEditCommand implements Command {
    private static final Logger logger = LogManager.getLogger(EmployeeEditCommand.class);
    private static final String EMPLOYEE_FORM_PAGE = "/WEB-INF/jsp/admin/employee-form.jsp";

    private final EmployeeService employeeService;

    public EmployeeEditCommand() {
        this.employeeService = new EmployeeService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long employeeId;
        try {
            employeeId = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            logger.error("Invalid employee ID", e);
            request.setAttribute("error", "Неверный ID сотрудника");
            response.sendRedirect(request.getContextPath() + "/app?command=employeeList");
            return null;
        }

        // GET-запрос - показываем форму для редактирования
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            Optional<Employee> employeeOptional = employeeService.findById(employeeId);

            if (employeeOptional.isEmpty()) {
                logger.warn("Employee with ID {} not found", employeeId);
                request.setAttribute("error", "Сотрудник не найден");
                response.sendRedirect(request.getContextPath() + "/app?command=employeeList");
                return null;
            }

            Employee employee = employeeOptional.get();
            request.setAttribute("employee", employee);
            request.setAttribute("active", "employees");
            request.setAttribute("formAction", "edit");

            // Получаем список возможных должностей
            List<EmployeePosition> positions = employeeService.getAllPositions();
            request.setAttribute("positions", positions);

            return EMPLOYEE_FORM_PAGE;
        }

        // POST-запрос - обрабатываем форму
        try {
            Optional<Employee> employeeOptional = employeeService.findById(employeeId);

            if (employeeOptional.isEmpty()) {
                logger.warn("Employee with ID {} not found for update", employeeId);
                request.setAttribute("error", "Сотрудник не найден");
                response.sendRedirect(request.getContextPath() + "/app?command=employeeList");
                return null;
            }

            Employee employee = employeeOptional.get();

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
                request.setAttribute("employee", employee);
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "edit");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            // Получаем позицию
            EmployeePosition position = employeeService.getPositionById(positionId).orElse(null);

            if (position == null) {
                request.setAttribute("error", "Выбрана несуществующая должность");
                request.setAttribute("employee", employee);
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "edit");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            // Валидация данных
            if (!ValidationUtil.isStringNotEmpty(firstName) ||
                    !ValidationUtil.isStringNotEmpty(lastName)) {

                request.setAttribute("error", "Имя и фамилия обязательны");
                request.setAttribute("employee", employee);
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "edit");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            if (passport != null && !passport.isEmpty() && !ValidationUtil.isValidPassport(passport)) {
                request.setAttribute("error", "Неверный формат паспортных данных");
                request.setAttribute("employee", employee);
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "edit");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }

            // Обновляем объект сотрудника
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setMiddleName(middleName);
            employee.setPosition(position);
            employee.setHireDate(hireDate);
            employee.setExperience(experience);
            employee.setPassport(passport);
            employee.setContactInfo(contactInfo);
            employee.setActive(active);

            // Сохраняем изменения
            boolean updated = employeeService.update(employee);

            if (updated) {
                logger.info("Updated employee with ID: {}", employee.getId());
                response.sendRedirect(request.getContextPath() + "/app?command=employeeList");
                return null;
            } else {
                logger.error("Failed to update employee with ID: {}", employee.getId());
                request.setAttribute("error", "Ошибка при обновлении сотрудника");
                request.setAttribute("employee", employee);
                request.setAttribute("active", "employees");
                request.setAttribute("formAction", "edit");
                List<EmployeePosition> positions = employeeService.getAllPositions();
                request.setAttribute("positions", positions);
                return EMPLOYEE_FORM_PAGE;
            }
        } catch (Exception e) {
            logger.error("Error updating employee", e);
            request.setAttribute("error", "Ошибка при обновлении сотрудника: " + e.getMessage());
            request.setAttribute("active", "employees");
            request.setAttribute("formAction", "edit");
            List<EmployeePosition> positions = employeeService.getAllPositions();
            request.setAttribute("positions", positions);
            return EMPLOYEE_FORM_PAGE;
        }
    }
}