// ValidationUtil.java
package com.airline.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Logger logger = LogManager.getLogger(ValidationUtil.class);

    // Регулярные выражения для валидации
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern FLIGHT_NUMBER_PATTERN =
            Pattern.compile("^[A-Z]{2}\\d{3,4}$");
    private static final Pattern PASSPORT_PATTERN =
            Pattern.compile("^[A-Za-z0-9]{6,15}$");

    private ValidationUtil() {
        // Приватный конструктор для утилитного класса
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidFlightNumber(String flightNumber) {
        return flightNumber != null && FLIGHT_NUMBER_PATTERN.matcher(flightNumber).matches();
    }

    public static boolean isValidPassport(String passport) {
        return passport != null && PASSPORT_PATTERN.matcher(passport).matches();
    }

    public static boolean isValidFutureDatetime(LocalDateTime datetime) {
        return datetime != null && datetime.isAfter(LocalDateTime.now());
    }

    public static boolean isValidPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isStringNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isPositiveNumber(Integer number) {
        return number != null && number > 0;
    }

    public static boolean isValidFlightTimes(LocalDateTime departure, LocalDateTime arrival) {
        return departure != null && arrival != null && departure.isBefore(arrival);
    }
}