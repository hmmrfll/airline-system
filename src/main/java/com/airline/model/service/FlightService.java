// FlightService.java
package com.airline.model.service;

import com.airline.model.dao.FlightDao;
import com.airline.model.entity.Flight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightService {
    private static final Logger logger = LogManager.getLogger(FlightService.class);
    private final FlightDao flightDao;

    public FlightService() {
        this.flightDao = new FlightDao();
    }

    public Optional<Flight> findById(Long id) {
        logger.info("Finding flight by id: {}", id);
        return flightDao.findById(id);
    }

    public List<Flight> findAll() {
        logger.info("Finding all flights");
        return flightDao.findAll();
    }

    public Flight save(Flight flight) {
        logger.info("Saving flight: {}", flight);
        return flightDao.save(flight);
    }

    public boolean update(Flight flight) {
        logger.info("Updating flight: {}", flight);
        return flightDao.update(flight);
    }

    public boolean delete(Long id) {
        logger.info("Deleting flight with id: {}", id);
        return flightDao.delete(id);
    }

    public boolean validateFlightData(Flight flight) {
        // Проверка корректности данных рейса
        boolean isNumberValid = flight.getFlightNumber() != null && !flight.getFlightNumber().isEmpty();
        boolean areAirportsValid = flight.getDepartureAirport() != null && !flight.getDepartureAirport().isEmpty() &&
                flight.getArrivalAirport() != null && !flight.getArrivalAirport().isEmpty();
        boolean areTimesValid = flight.getDepartureTime() != null && flight.getArrivalTime() != null &&
                flight.getDepartureTime().isBefore(flight.getArrivalTime());
        boolean isAircraftValid = flight.getAircraftType() != null && !flight.getAircraftType().isEmpty();

        return isNumberValid && areAirportsValid && areTimesValid && isAircraftValid;
    }

    public List<Flight> findUpcomingFlights() {
        // Получаем все рейсы
        List<Flight> allFlights = flightDao.findAll();

        // Фильтруем только предстоящие
        LocalDateTime now = LocalDateTime.now();
        return allFlights.stream()
                .filter(flight -> flight.getDepartureTime().isAfter(now) &&
                        !"CANCELLED".equals(flight.getStatus()))
                .toList();
    }
}