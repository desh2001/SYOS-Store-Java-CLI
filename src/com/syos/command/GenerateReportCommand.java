package com.syos.command;

import com.syos.dao.ReportDAO;
import java.util.Scanner;

/**
 * Command Pattern + Template Method Pattern
 * Encapsulates the report generation sub-menu and dispatches to ReportDAO
 * (which internally uses Template Method pattern report classes).
 */
public class GenerateReportCommand implements Command {
    private Scanner scanner;

    public GenerateReportCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n┌──────────────────────────────────────────┐");
            System.out.println("│             REPORTS SYSTEM               │");
            System.out.println("├──────────────────────────────────────────┤");
            System.out.println("│  1. Daily Sales Report                   │");
            System.out.println("│  2. Reshelving Report                    │");
            System.out.println("│  3. Reorder Level Report                 │");
            System.out.println("│  4. Warehouse Stock Report               │");
            System.out.println("│  5. All Transactions Report              │");
            System.out.println("└──────────────────────────────────────────┘");
            System.out.print(" >>> Select Report Type (1-5): ");

            int rChoice = Integer.parseInt(scanner.nextLine());
            ReportDAO reportDAO = new ReportDAO();
            switch (rChoice) {
                case 1: reportDAO.generateDailySalesReport(); break;
                case 2: reportDAO.generateReshelvingReport(); break;
                case 3: reportDAO.generateReorderReport(); break;
                case 4: reportDAO.generateStockReport(); break;
                case 5: reportDAO.generateBillReport(); break;
                default: System.out.println(" [!] Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("\n [!] Error: " + e.getMessage());
        }
    }
}
