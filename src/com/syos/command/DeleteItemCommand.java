package com.syos.command;

import com.syos.gateway.ItemGateway;
import java.util.Scanner;

/**
 * Command Pattern — Encapsulates the "Delete Item" action.
 */
public class DeleteItemCommand implements Command {
    private Scanner scanner;
    private ItemGateway itemDAO;

    public DeleteItemCommand(Scanner scanner, ItemGateway itemDAO) {
        this.scanner = scanner;
        this.itemDAO = itemDAO;
    }

    @Override
    public void execute() {
        try {
            System.out.print(" [-] Enter Item Code to Delete: ");
            String code = scanner.nextLine().trim();
            System.out.print(" [?] Are you sure? (yes/no): ");
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                itemDAO.deleteItemByCode(code);
            } else {
                System.out.println(" [!] Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println(" [!] Error: " + e.getMessage());
        }
    }
}
