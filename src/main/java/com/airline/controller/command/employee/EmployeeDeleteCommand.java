// EmployeeDeleteCommand.java
package com.airline.controller.command.employee;

import com.airline.controller.command.Command;
import com.airline.model.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmployeeDeleteCommand implements Command {
    private static final Logger logger = LogManager.getLogger(EmployeeDeleteCommand.class);

    private final EmployeeService employeeService;

    public EmployeeDeleteCommand() {
        this.employeeService = new EmployeeService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long employeeId = Long.parseLong(request.getParameter("id"));
            boolean deleted = employeeService.delete(employeeId);

            if (deleted) {
                logger.info("Deleted employee with ID: {}", employeeId);
                request.setAttribute("message", "Сотрудник успешно удален");
            } else {
                logger.warn("Failed to delete employee with ID: {}", employeeId);
                request.setAttribute("error", "Невозможно удалить сотрудника");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid employee ID for deletion", e);
            request.setAttribute("error", "Неверный ID сотрудника");
        }

        response.sendRedirect(request.getContextPath() + "/app?command=employeeList");
        return null;
    }
}