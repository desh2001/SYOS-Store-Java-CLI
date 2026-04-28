package com.syos.state;

import com.syos.factory.DAOFactory;
import com.syos.view.OnlineStoreCLI;
import java.util.Scanner;

public class CustomerMenuState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║          CUSTOMER DASHBOARD              ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Store                                ║");
        System.out.println("║  2. Logout                               ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print(" >>> Select an option (1-2): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":

                new OnlineStoreCLI(scanner, factory).start();
                return this;

            case "2":
                System.out.println("\n [*] Logging out...\n");
                return new RoleSelectionState();

            default:
                System.out.println(" [!] Invalid choice. Please select 1 or 2.");
                return this;
        }
    }
}


