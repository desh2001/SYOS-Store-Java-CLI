package com.syos.state;

import com.syos.command.*;
import com.syos.factory.DAOFactory;
import java.util.Scanner;

/**
 * State Pattern + Command Pattern — Main menu state.
 * Displays the main menu options and dispatches Command objects.
 * Transitions to InventoryMenuState, or back to LoginState on logout.
 */
public class MainMenuState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           WELCOME TO SYOS MENU           ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Inventory Management (Items & Stock) ║");
        System.out.println("║  2. Create Bill (POS)                    ║");
        System.out.println("║  3. Online Store                         ║");
        System.out.println("║  4. Reports                              ║");
        System.out.println("║  5. Logout                               ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print(" >>> Select an option (1-5): ");

        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" [!] Please enter a valid number.");
            return this; // Stay in MainMenuState
        }

        switch (choice) {
            case 1:
                // State transition → InventoryMenuState
                return new InventoryMenuState();

            case 2:
                // Command Pattern — Create Bill
                Command billCmd = new CreateBillCommand(scanner, 
                    factory.createItemDAO(), factory.createStockDAO(), factory.createBillDAO());
                billCmd.execute();
                return this; // Return to MainMenuState

            case 3:
                // Command Pattern — Open Online Store
                Command onlineCmd = new OpenOnlineStoreCommand(scanner, factory);
                onlineCmd.execute();
                return this; // Return to MainMenuState

            case 4:
                // Command Pattern — Generate Report
                Command reportCmd = new GenerateReportCommand(scanner);
                reportCmd.execute();
                return this; // Return to MainMenuState

            case 5:
                System.out.println("\n [*] Logging out...\n");
                // State transition → LoginState
                return new LoginState();

            default:
                System.out.println(" [!] Invalid choice. Please select from 1-5.");
                return this; // Stay in MainMenuState
        }
    }
}
