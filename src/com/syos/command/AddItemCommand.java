package com.syos.command;

import com.syos.gateway.ItemGateway;
import com.syos.model.Item;
import java.util.Scanner;

/**
 * Command Pattern — Encapsulates the "Add New Item" action.
 */
public class AddItemCommand implements Command {
    private Scanner scanner;
    private ItemGateway itemDAO;

    public AddItemCommand(Scanner scanner, ItemGateway itemDAO) {
        this.scanner = scanner;
        this.itemDAO = itemDAO;
    }

    @Override
    public void execute() {
        try {
            System.out.print(" [+] Item Code: "); String code = scanner.nextLine();
            System.out.print(" [+] Item Name: "); String name = scanner.nextLine();
            System.out.print(" [+] Price (LKR): "); double price = Double.parseDouble(scanner.nextLine());
            itemDAO.saveItem(new Item(code, name, price));
            System.out.println(" [OK] Item added successfully: " + name);
        } catch (Exception e) {
            System.out.println(" [!] Error: " + e.getMessage());
        }
    }
}
