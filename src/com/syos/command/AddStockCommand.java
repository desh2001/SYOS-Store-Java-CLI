package com.syos.command;

import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import com.syos.model.Item;
import com.syos.model.Stock;
import java.util.Scanner;

/**
 * Command Pattern — Encapsulates the "Add Stock to Warehouse" action.
 */
public class AddStockCommand implements Command {
    private Scanner scanner;
    private ItemGateway itemDAO;
    private StockGateway stockDAO;

    public AddStockCommand(Scanner scanner, ItemGateway itemDAO, StockGateway stockDAO) {
        this.scanner = scanner;
        this.itemDAO = itemDAO;
        this.stockDAO = stockDAO;
    }

    @Override
    public void execute() {
        try {
            System.out.print(" [+] Item Code: ");
            String addCode = scanner.nextLine().trim();

            // Code එකෙන් Item එක හොයාගන්නවා
            Item addItem = itemDAO.getItemByCode(addCode);

            if (addItem != null) {
                System.out.print(" [+] Batch No: "); String batchNo = scanner.nextLine();
                System.out.print(" [+] Quantity: "); int qty = Integer.parseInt(scanner.nextLine());
                System.out.print(" [+] Expiry Date (yyyy-MM-dd): ");
                java.util.Date expiry = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());

                // හොයාගත්ත Item එකේ ID එක මෙතනින් pass කරනවා
                stockDAO.addStock(new Stock(addItem.getId(), batchNo, qty, expiry));
                System.out.println(" [OK] Stock added successfully!");
            } else {
                System.out.println(" [!] Item not found with Code: " + addCode);
            }
        } catch (Exception e) {
            System.out.println(" [!] Error: " + e.getMessage());
        }
    }
}
