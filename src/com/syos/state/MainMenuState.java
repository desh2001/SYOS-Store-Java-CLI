package com.syos.state;

import com.syos.command.*;
import com.syos.factory.DAOFactory;
import java.util.Scanner;

public class MainMenuState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           SYOS ADMIN DASHBOARD           ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Inventory Management (Items & Stock) ║");
        System.out.println("║  2. Create Bill (POS)                    ║");
        System.out.println("║  3. Reports                              ║");
        System.out.println("║  4. Logout                               ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print(" >>> Select an option (1-4): ");

        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" [!] Please enter a valid number.");
            return this;
        }

        switch (choice) {
            case 1:
                return new InventoryMenuState();

            case 2:
                Command billCmd = new CreateBillCommand(scanner,
                    factory.createItemDAO(), factory.createStockDAO(), factory.createBillDAO());
                billCmd.execute();
                return this;

            case 3:
                Command reportCmd = new GenerateReportCommand(scanner);
                reportCmd.execute();
                return this;

            case 4:
                System.out.println("\n [*] Logging out...\n");
                return new RoleSelectionState();

            default:
                System.out.println(" [!] Invalid choice. Please select from 1-4.");
                return this;
        }
    }
}


