package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

public class RoleSelectionState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n\n");
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       WELCOME TO SYOS STORE SYSTEM       ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Select your role:                       ║");
        System.out.println("║  1. Admin                                ║");
        System.out.println("║  2. Customer                             ║");
        System.out.println("║  3. Exit                                 ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print(" >>> Choice (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                return new LoginState();
            case "2":
                return new CustomerLoginState();
            case "3":
                System.out.println("\n [*] Exiting system...");
                return null;
            default:
                System.out.println(" [!] Invalid choice. Please select 1, 2, or 3.");
                return this;
        }
    }
}


