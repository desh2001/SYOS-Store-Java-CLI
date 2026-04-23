package com.syos.command;

import com.syos.gateway.ItemGateway;
import java.util.Scanner;

/**
 * Command Pattern — Encapsulates the "Update Item Details" action.
 */
public class UpdateItemCommand implements Command {
    private Scanner scanner;
    private ItemGateway itemDAO;

    public UpdateItemCommand(Scanner scanner, ItemGateway itemDAO) {
        this.scanner = scanner;
        this.itemDAO = itemDAO;
    }

    @Override
    public void execute() {
        try {
            System.out.print(" [~] Enter Item Code to Update: ");
            String code = scanner.nextLine().trim();
            System.out.print(" [~] New Name: ");
            String name = scanner.nextLine();
            System.out.print(" [~] New Price (LKR): ");
            double price = Double.parseDouble(scanner.nextLine());
            itemDAO.updateItemByCode(code, name, price);
        } catch (Exception e) {
            System.out.println(" [!] Error: " + e.getMessage());
        }
    }
}
