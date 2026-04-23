package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

/**
 * State Pattern — Login state.
 * Displays the login prompt and validates credentials.
 * Transitions to MainMenuState on success, stays in LoginState on failure.
 */
public class LoginState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n\n");
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         SYOS SYSTEM ADMIN LOGIN          ║");
        System.out.println("╚══════════════════════════════════════════╝");

        while (true) {
            System.out.print(" [>] Enter Username: ");
            String username = scanner.nextLine().trim();

            System.out.print(" [>] Enter Password: ");
            String password = scanner.nextLine().trim();

            if (username.equals("admin") && password.equals("admin123")) {
                System.out.println("\n [OK] Login successful! Welcome, " + username + ".");
                // State transition → MainMenuState
                return new MainMenuState();
            } else {
                System.out.println(" [!] Invalid username or password. Please try again.\n");
            }
        }
    }
}
