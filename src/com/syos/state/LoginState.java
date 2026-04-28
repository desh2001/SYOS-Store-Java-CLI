package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

public class LoginState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n\n");
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         SYOS SYSTEM ADMIN LOGIN          ║");
        System.out.println("╚══════════════════════════════════════════╝");

        while (true) {
            System.out.println("\n [!] Enter 'back' to return to role selection.");
            System.out.print(" [>] Enter Admin Username: ");
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("back")) return new RoleSelectionState();

            System.out.print(" [>] Enter Admin Password: ");
            String password = scanner.nextLine().trim();

            if (username.equals("admin") && password.equals("admin123")) {
                System.out.println("\n [OK] Admin login successful! Welcome.");

                return new MainMenuState();
            } else {
                System.out.println(" [!] Invalid admin credentials. Please try again.\n");
            }
        }
    }
}


