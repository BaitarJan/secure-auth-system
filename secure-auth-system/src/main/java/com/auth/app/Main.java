
package com.auth.app;

import com.auth.db.DatabaseManager;
import com.auth.repository.SqliteUserRepository;
import com.auth.repository.UserRepository;
import com.auth.service.AuthService;
import com.auth.service.LockService;
import com.auth.service.ValidationService;
import com.auth.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        try {
            DatabaseManager.init();
            Connection conn = DatabaseManager.connect();


            UserRepository repo = new SqliteUserRepository(conn);
            ValidationService validationService = new ValidationService();
            LockService lockService = new LockService();
            AuthService authService = new AuthService(repo, validationService, lockService);

            while (true) {
                System.out.println("\n=== MENU ===");
                System.out.println("1 - Register");
                System.out.println("2 - Login");
                System.out.println("0 - Exit");

                String choice = sc.nextLine();

                switch (choice) {

                    case "1":
                        System.out.print("Username: ");
                        String username = sc.nextLine();

                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        System.out.print("Password: ");
                        String password = sc.nextLine();

                        authService.register(username, email, password);
                        break;

                    case "2":
                        System.out.print("Username: ");
                        String loginUser = sc.nextLine();

                        System.out.print("Password: ");
                        String loginPass = sc.nextLine();

                        boolean success = authService.login(loginUser, loginPass);

                        if (success) {
                            System.out.println("Welcome!");
                        } else {
                            System.out.println("Login failed.");

                        }
                        break;

                    case "0":
                        System.out.println("Bye!");
                        return;

                    default:
                        System.out.println("Invalid option");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
