package com.airline.model.entity;

public class CrewMember {
    private Long id;
    private Crew crew;
    private Employee employee;

    public CrewMember() {
    }

    public CrewMember(Long id, Crew crew, Employee employee) {
        this.id = id;
        this.crew = crew;
        this.employee = employee;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "CrewMember{" +
                "id=" + id +
                ", crew=" + crew +
                ", employee=" + employee +
                '}';
    }
}