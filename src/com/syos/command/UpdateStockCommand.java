package com.syos.command;

import com.syos.gateway.StockGateway;
import java.util.Scanner;

/**
 * Command Pattern — Encapsulates the "Update Warehouse Stock Qty" action.
 */
public class UpdateStockCommand implements Command {
    private Scanner scanner;
    private StockGateway stockDAO;

    public UpdateStockCommand(Scanner scanner, StockGateway stockDAO) {
        this.scanner = scanner;
        this.stockDAO = stockDAO;
    }

    @Override
    public void execute() {
        try {
            // Stock එක Update කරන්න Item Code එකයි, අදාළ Batch No එකයි ඉල්ලනවා
            System.out.print(" [~] Enter Item Code: "); String code = scanner.nextLine().trim();
            System.out.print(" [~] Enter Batch No: "); String batch = scanner.nextLine().trim();
            System.out.print(" [~] New Quantity: "); int qty = Integer.parseInt(scanner.nextLine());
            stockDAO.updateStockByCodeAndBatch(code, batch, qty);
        } catch (Exception e) {
            System.out.println(" [!] Error: " + e.getMessage());
        }
    }
}
