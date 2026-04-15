package com.auth.service;

import com.auth.model.User;
import com.auth.repository.UserRepository;

import com.auth.util.PasswordUtil;

import java.time.format.DateTimeFormatter;

public class AuthService {



    private UserRepository userRepository;
    private ValidationService validationService;
    private LockService lockService;
    public AuthService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    public AuthService(UserRepository userRepository,
                       ValidationService validationService,
                       LockService lockService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.lockService = lockService;
    }



    public boolean login(String username, String password) {

        if (!validationService.isValidUsername(username)) {
            System.out.println("Invalid credentials");

            return false;
        }

        if (!validationService.isValidPassword(password)) {
            System.out.println("Invalid credentials");
            return false;
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("Invalid credentials");
            return false;
        }

        // pokud lock existuje a už vypršel, resetuj ho
        if (user.getLockUntil() != null && !lockService.isLocked(user.getLockUntil())) {
            user.setFailedAttempts(0);
            user.setLockUntil(null);
            userRepository.update(user);
        }

        // pokud lock pořád platí, stop
        if (lockService.isLocked(user.getLockUntil())) {
            System.out.println("Account temporarily locked until " + user.getLockUntil().format(formatter));
            return false;
        }


        // 🔑 kontrola hesla
        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= 3) {
                user.setLockUntil(lockService.lockForMinutes(10));
            }

            userRepository.update(user);
            return false;

        }

        // ✅ úspěch → reset pokusů
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        userRepository.update(user);

        System.out.println("Login successful");
        return true;
    }
    public void register(String username, String email, String password) {

        if (!validationService.isValidUsername(username)) {
            System.out.println("Invalid username");
            return;
        }

        if (!validationService.isValidEmail(email)) {
            System.out.println("Invalid email");
            return;
        }

        if (!validationService.isValidPassword(password)) {
            System.out.println("Invalid password");
            return;
        }

        User existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            System.out.println("User already exists");
            return;
        }

        String passwordHash = PasswordUtil.hashPassword(password);

        User user = new User(username, email, passwordHash);

        userRepository.save(user);

        System.out.println("User registered!");
    }




}
