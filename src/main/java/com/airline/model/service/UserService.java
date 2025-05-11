// UserService.java
package com.airline.model.service;

import com.airline.model.dao.UserDao;
import com.airline.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public Optional<User> findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return userDao.findById(id);
    }

    public List<User> findAll() {
        logger.info("Finding all users");
        return userDao.findAll();
    }

    public Optional<User> findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return userDao.findByUsername(username);
    }

    public User save(User user) {
        logger.info("Saving new user: {}", user);
        return userDao.save(user);
    }

    public boolean update(User user) {
        logger.info("Updating user: {}", user);
        return userDao.update(user);
    }

    public boolean delete(Long id) {
        logger.info("Deleting user with id: {}", id);
        return userDao.delete(id);
    }

    public Optional<User> authenticate(String username, String password) {
        logger.info("Authenticating user: {}", username);
        Optional<User> userOptional = userDao.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // В реальном приложении здесь должна быть проверка хеша пароля
            // Для простоты примера используем прямое сравнение паролей
            if (password.equals(user.getPassword()) && user.isActive()) {
                return Optional.of(user);
            }
        }

        logger.warn("Authentication failed for user: {}", username);
        return Optional.empty();
    }
}