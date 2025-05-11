// CommandFactory.java
package com.airline.controller.command;

import com.airline.controller.command.flight.*;
import com.airline.controller.command.crew.*;
import com.airline.controller.command.employee.*;
import com.airline.controller.command.user.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final Logger logger = LogManager.getLogger(CommandFactory.class);
    private static CommandFactory instance;
    private final Map<String, Command> commands;

    private CommandFactory() {
        commands = new HashMap<>();

        // User commands
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());

        commands.put("register", new RegisterCommand());

        // Flight commands
        commands.put("flightList", new FlightListCommand());
        commands.put("flightCreate", new FlightCreateCommand());
        commands.put("flightEdit", new FlightEditCommand());
        commands.put("flightDelete", new FlightDeleteCommand());

        // Employee commands
        commands.put("employeeList", new EmployeeListCommand());
        commands.put("employeeCreate", new EmployeeCreateCommand());
        commands.put("employeeEdit", new EmployeeEditCommand());
        commands.put("employeeDelete", new EmployeeDeleteCommand());

        // Crew commands
        commands.put("crewList", new CrewListCommand());
        commands.put("crewCreate", new CrewCreateCommand());
        commands.put("crewEdit", new CrewEditCommand());
        commands.put("crewDelete", new CrewDeleteCommand());
        commands.put("crewAddMember", new CrewAddMemberCommand());
        commands.put("crewRemoveMember", new CrewRemoveMemberCommand());
    }

    public static synchronized CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    public Command getCommand(String commandName) {
        Command command = commands.get(commandName);
        if (command == null) {
            logger.warn("Command not found: {}", commandName);
            command = commands.get("flightList"); // Default command
        }
        return command;
    }
}