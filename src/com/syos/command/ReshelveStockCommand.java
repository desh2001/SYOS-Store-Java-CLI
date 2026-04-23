package com.syos.command;

import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import com.syos.model.Item;
import com.syos.strategy.FEFOStrategy;
import java.util.Scanner;

/**
 * Command Pattern + Strategy Pattern — Encapsulates the "Stock Transfer (Warehouse to Shelf)" action.
 * Uses the FEFO strategy by default for stock depletion ordering.
 */
public class ReshelveStockCommand implements Command {
    private Scanner scanner;
    private ItemGateway itemDAO;
    private StockGateway stockDAO;

    public ReshelveStockCommand(Scanner scanner, ItemGateway itemDAO, StockGateway stockDAO) {
        this.scanner = scanner;
        this.itemDAO = itemDAO;
        this.stockDAO = stockDAO;
    }

    @Override
    public void execute() {
        try {
            System.out.print(" [>] Enter Item Code to Reshelve: ");
            String resCode = scanner.nextLine().trim();

            // Code එකෙන් Item එක හොයාගන්නවා
            Item resItem = itemDAO.getItemByCode(resCode);

            if (resItem != null) {
                System.out.print(" [>] Enter Quantity to move to Shelf: ");
                int reshelveQty = Integer.parseInt(scanner.nextLine());

                // Strategy Pattern — using FEFO strategy (default)
                boolean success = stockDAO.reshelveItem(resItem.getId(), reshelveQty, new FEFOStrategy());
                if (success) {
                    System.out.println(" [OK] Successfully reshelved " + reshelveQty + " units using FEFO strategy.");
                } else {
                    System.out.println(" [!] Not enough stock in warehouse.");
                }
            } else {
                System.out.println(" [!] Item not found with Code: " + resCode);
            }
        } catch (Exception e) {
            System.out.println(" [!] Error: " + e.getMessage());
        }
    }
}
