package com.syos.command;

import com.syos.dao.ReportDAO;
import java.util.Scanner;

public class GenerateReportCommand implements Command {
    private Scanner scanner;

    public GenerateReportCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        ReportDAO reportDAO = new ReportDAO();
        while (true) {
            try {
                System.out.println("\n┌──────────────────────────────────────────┐");
                System.out.println("│             REPORTS SYSTEM               │");
                System.out.println("├──────────────────────────────────────────┤");
                System.out.println("│  1. Daily Sales Report                   │");
                System.out.println("│  2. Reshelving Report                    │");
                System.out.println("│  3. Reorder Level Report                 │");
                System.out.println("│  4. Warehouse Stock Report               │");
                System.out.println("│  5. All Transactions Report              │");
                System.out.println("│  6. Back to Main Menu                    │");
                System.out.println("└──────────────────────────────────────────┘");
                System.out.print(" >>> Select Report Type (1-6): ");

                int rChoice = Integer.parseInt(scanner.nextLine());

                if (rChoice == 6) {
                    break;
                }

                if (rChoice >= 1 && rChoice <= 5) {
                    reportDAO.generateReport(rChoice);
                } else {
                    System.out.println(" [!] Invalid selection.");
                }

                System.out.println("\n [*] Press Enter to return to Reports Menu...");
                scanner.nextLine();

            } catch (Exception e) {
                System.out.println("\n [!] Error: " + e.getMessage());
            }
        }
    }
}


