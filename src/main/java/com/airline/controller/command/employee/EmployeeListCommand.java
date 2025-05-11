// EmployeeListCommand.java
package com.airline.controller.command.employee;

import com.airline.controller.command.Command;
import com.airline.model.entity.Employee;
import com.airline.model.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EmployeeListCommand implements Command {
    private static final Logger logger = LogManager.getLogger(EmployeeListCommand.class);
    private static final String EMPLOYEES_PAGE = "/WEB-INF/jsp/admin/employees.jsp";

    private final EmployeeService employeeService;

    public EmployeeListCommand() {
        this.employeeService = new EmployeeService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Employee> employees = employeeService.findAll();
        request.setAttribute("employees", employees);
        request.setAttribute("active", "employees");

        logger.info("Retrieved {} employees for display", employees.size());
        return EMPLOYEES_PAGE;
    }
}