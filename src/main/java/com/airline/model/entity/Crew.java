package com.airline.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Crew {
    private Long id;
    private Flight flight;
    private String name;
    private User createdBy;
    private LocalDateTime createdAt;
    private List<Employee> members = new ArrayList<>();

    // Builder pattern
    public static class Builder {
        private Crew crew;

        public Builder() {
            crew = new Crew();
        }

        public Builder withId(Long id) {
            crew.id = id;
            return this;
        }

        public Builder withFlight(Flight flight) {
            crew.flight = flight;
            return this;
        }

        public Builder withName(String name) {
            crew.name = name;
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
            crew.createdBy = createdBy;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            crew.createdAt = createdAt;
            return this;
        }

        public Builder withMembers(List<Employee> members) {
            crew.members = members;
            return this;
        }

        public Crew build() {
            return crew;
        }
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Employee> getMembers() {
        return members;
    }

    public void setMembers(List<Employee> members) {
        this.members = members;
    }

    public void addMember(Employee employee) {
        if (!members.contains(employee)) {
            members.add(employee);
        }
    }

    public void removeMember(Employee employee) {
        members.remove(employee);
    }

    @Override
    public String toString() {
        return "Crew{" +
                "id=" + id +
                ", flight=" + flight +
                ", name='" + name + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", members=" + members +
                '}';
    }
}