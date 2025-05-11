package com.airline.model.entity;

import java.time.LocalDateTime;

public class Flight {
    private Long id;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String aircraftType;
    private String status;
    private User createdBy;
    private LocalDateTime createdAt;

    // Builder pattern
    public static class Builder {
        private Flight flight;

        public Builder() {
            flight = new Flight();
        }

        public Builder withId(Long id) {
            flight.id = id;
            return this;
        }

        public Builder withFlightNumber(String flightNumber) {
            flight.flightNumber = flightNumber;
            return this;
        }

        public Builder withDepartureAirport(String departureAirport) {
            flight.departureAirport = departureAirport;
            return this;
        }

        public Builder withArrivalAirport(String arrivalAirport) {
            flight.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder withDepartureTime(LocalDateTime departureTime) {
            flight.departureTime = departureTime;
            return this;
        }

        public Builder withArrivalTime(LocalDateTime arrivalTime) {
            flight.arrivalTime = arrivalTime;
            return this;
        }

        public Builder withAircraftType(String aircraftType) {
            flight.aircraftType = aircraftType;
            return this;
        }

        public Builder withStatus(String status) {
            flight.status = status;
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
            flight.createdBy = createdBy;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            flight.createdAt = createdAt;
            return this;
        }

        public Flight build() {
            return flight;
        }
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", aircraftType='" + aircraftType + '\'' +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}