package com.auth.service;


    public class ValidationService {

        public boolean isValidUsername(String username) {
            return username != null && username.length() >= 3;
        }

        public boolean isValidPassword(String password) {
            return password != null && password.length() >= 5;
        }

        public boolean isValidEmail(String email) {
            return email != null && email.contains("@");
        }
    }

