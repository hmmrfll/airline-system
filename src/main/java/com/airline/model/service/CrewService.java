// CrewService.java
package com.airline.model.service;

import com.airline.model.dao.CrewDao;
import com.airline.model.dao.EmployeeDao;
import com.airline.model.entity.Crew;
import com.airline.model.entity.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class CrewService {
    private static final Logger logger = LogManager.getLogger(CrewService.class);
    private final CrewDao crewDao;
    private final EmployeeDao employeeDao;

    public CrewService() {
        this.crewDao = new CrewDao();
        this.employeeDao = new EmployeeDao();
    }

    public Optional<Crew> findById(Long id) {
        logger.info("Finding crew by id: {}", id);
        return crewDao.findById(id);
    }

    public List<Crew> findAll() {
        logger.info("Finding all crews");
        return crewDao.findAll();
    }

    public List<Crew> findByFlightId(Long flightId) {
        logger.info("Finding crews by flight id: {}", flightId);
        return crewDao.findByFlightId(flightId);
    }

    public Crew save(Crew crew) {
        logger.info("Saving crew: {}", crew);
        return crewDao.save(crew);
    }

    public boolean update(Crew crew) {
        logger.info("Updating crew: {}", crew);
        return crewDao.update(crew);
    }

    public boolean delete(Long id) {
        logger.info("Deleting crew with id: {}", id);
        return crewDao.delete(id);
    }

    public boolean addMember(Long crewId, Long employeeId) {
        logger.info("Adding employee {} to crew {}", employeeId, crewId);
        return crewDao.addMember(crewId, employeeId);
    }

    public boolean removeMember(Long crewId, Long employeeId) {
        logger.info("Removing employee {} from crew {}", employeeId, crewId);
        return crewDao.removeMember(crewId, employeeId);
    }

    public List<Employee> getAvailableEmployees(Long crewId) {
        // Получаем всех активных сотрудников
        List<Employee> allEmployees = employeeDao.findAll().stream()
                .filter(Employee::isActive)
                .toList();

        // Получаем сотрудников, уже входящих в бригаду
        List<Employee> crewMembers = employeeDao.findByCrewId(crewId);

        // Исключаем тех, кто уже в бригаде
        return allEmployees.stream()
                .filter(employee -> !crewMembers.contains(employee))
                .toList();
    }

    public boolean checkCrewCompletion(Crew crew) {
        // Проверка на полноту экипажа (валидация бизнес-правил)
        boolean hasPilot = crew.getMembers().stream()
                .anyMatch(e -> "PILOT".equals(e.getPosition().getName()));
        boolean hasCoPilot = crew.getMembers().stream()
                .anyMatch(e -> "CO_PILOT".equals(e.getPosition().getName()));
        boolean hasNavigator = crew.getMembers().stream()
                .anyMatch(e -> "NAVIGATOR".equals(e.getPosition().getName()));
        boolean hasRadioOperator = crew.getMembers().stream()
                .anyMatch(e -> "RADIO_OPERATOR".equals(e.getPosition().getName()));
        boolean hasSteward = crew.getMembers().stream()
                .anyMatch(e -> "STEWARD".equals(e.getPosition().getName()) ||
                        "STEWARDESS".equals(e.getPosition().getName()));

        return hasPilot && hasCoPilot && hasNavigator && hasRadioOperator && hasSteward;
    }
}