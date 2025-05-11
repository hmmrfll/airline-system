package com.airline.model.entity;

import java.time.LocalDate;

public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private EmployeePosition position;
    private LocalDate hireDate;
    private Integer experience;
    private String passport;
    private String contactInfo;
    private boolean active;

    // Builder pattern
    public static class Builder {
        private Employee employee;

        public Builder() {
            employee = new Employee();
        }

        public Builder withId(Long id) {
            employee.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            employee.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            employee.lastName = lastName;
            return this;
        }

        public Builder withMiddleName(String middleName) {
            employee.middleName = middleName;
            return this;
        }

        public Builder withPosition(EmployeePosition position) {
            employee.position = position;
            return this;
        }

        public Builder withHireDate(LocalDate hireDate) {
            employee.hireDate = hireDate;
            return this;
        }

        public Builder withExperience(Integer experience) {
            employee.experience = experience;
            return this;
        }

        public Builder withPassport(String passport) {
            employee.passport = passport;
            return this;
        }

        public Builder withContactInfo(String contactInfo) {
            employee.contactInfo = contactInfo;
            return this;
        }

        public Builder withActive(boolean active) {
            employee.active = active;
            return this;
        }

        public Employee build() {
            return employee;
        }
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullName() {
        return lastName + " " + firstName + (middleName != null ? " " + middleName : "");
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", position=" + position +
                ", hireDate=" + hireDate +
                ", experience=" + experience +
                ", passport='" + passport + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", active=" + active +
                '}';
    }
}