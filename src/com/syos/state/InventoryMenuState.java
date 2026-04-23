package com.syos.state;

import com.syos.command.*;
import com.syos.factory.DAOFactory;
import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import java.util.Scanner;

/**
 * State Pattern + Command Pattern — Inventory management sub-menu state.
 * Displays inventory options and dispatches corresponding Command objects.
 * Transitions back to MainMenuState when user selects "Back".
 */
public class InventoryMenuState implements AppState {

    @Override
    public AppState handleState(Scanner scanner, DAOFactory factory) {
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│           INVENTORY MANAGEMENT           │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Add New Item                         │");
        System.out.println("│  2. Update Item Details                  │");
        System.out.println("│  3. Delete Item                          │");
        System.out.println("│  4. Add Stock to Warehouse               │");
        System.out.println("│  5. Update Warehouse Stock Qty           │");
        System.out.println("│  6. Stock Transfer (Warehouse to Shelf)  │");
        System.out.println("│  7. Back to Main Menu                    │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print(" >>> Select Sub-option (1-7): ");

        int invChoice = -1;
        try {
            invChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" [!] Please enter a valid number.");
            return this; // Stay in InventoryMenuState
        }

        // Factory Pattern — create DAOs from factory
        ItemGateway itemDAO = factory.createItemDAO();
        StockGateway stockDAO = factory.createStockDAO();

        System.out.println();

        // Command Pattern — dispatch to appropriate command
        Command command = null;
        switch (invChoice) {
            case 1:
                command = new AddItemCommand(scanner, itemDAO);
                break;
            case 2:
                command = new UpdateItemCommand(scanner, itemDAO);
                break;
            case 3:
                command = new DeleteItemCommand(scanner, itemDAO);
                break;
            case 4:
                command = new AddStockCommand(scanner, itemDAO, stockDAO);
                break;
            case 5:
                command = new UpdateStockCommand(scanner, stockDAO);
                break;
            case 6:
                command = new ReshelveStockCommand(scanner, itemDAO, stockDAO);
                break;
            case 7:
                System.out.println(" [<] Returning to Main Menu...");
                // State transition → MainMenuState
                return new MainMenuState();
            default:
                System.out.println(" [!] Invalid Selection.");
                return this; // Stay in InventoryMenuState
        }

        if (command != null) {
            command.execute();
        }

        return this; // Stay in InventoryMenuState after executing command
    }
}
