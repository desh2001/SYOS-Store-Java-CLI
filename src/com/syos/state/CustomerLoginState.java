package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

public class CustomerLoginState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n\n");
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║            CUSTOMER LOGIN                ║");
        System.out.println("╚══════════════════════════════════════════╝");

        while (true) {
            System.out.println("\n [!] Enter 'back' to return to role selection.");
            System.out.print(" [>] Enter Username: ");
            String username = scanner.nextLine().trim();

            if (username.equalsIgnoreCase("back")) return new RoleSelectionState();

            System.out.print(" [>] Enter Password: ");
            String password = scanner.nextLine().trim();

            if (username.equals("apiit") && password.equals("apiit123")) {
                System.out.println("\n [OK] Customer login successful! Welcome.");
                return new CustomerMenuState();
            } else {
                System.out.println(" [!] Invalid customer credentials. Please try again.\n");
            }
        }
    }
}


