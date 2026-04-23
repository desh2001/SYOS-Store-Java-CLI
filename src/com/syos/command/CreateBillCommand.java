package com.syos.command;

import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import com.syos.gateway.BillGateway;
import com.syos.model.Bill;
import com.syos.model.BillItem;
import com.syos.model.Item;
import com.syos.decorator.*;
import java.util.Scanner;

/**
 * Command Pattern + Decorator Pattern + DTO Pattern
 * Encapsulates the entire POS billing process.
 * Uses Bill and BillItem DTOs instead of raw arrays.
 * Uses Decorator pattern for optional discount/tax application.
 */
public class CreateBillCommand implements Command {
    private Scanner scanner;
    private ItemGateway itemDAO;
    private StockGateway stockDAO;
    private BillGateway billDAO;

    public CreateBillCommand(Scanner scanner, ItemGateway itemDAO, StockGateway stockDAO, BillGateway billDAO) {
        this.scanner = scanner;
        this.itemDAO = itemDAO;
        this.stockDAO = stockDAO;
        this.billDAO = billDAO;
    }

    @Override
    public void execute() {
        try {
            // DTO Pattern — Using Bill object instead of loose variables
            Bill bill = new Bill("OFFLINE");

            System.out.println("\n --- ADD ITEMS TO BILL ---");
            System.out.println(" (Enter 'done' or '0' to Finish)\n");

            while (true) {
                System.out.print(" [*] Enter Item Code: ");
                String bItemCode = scanner.nextLine().trim();
                if (bItemCode.equalsIgnoreCase("done") || bItemCode.equals("0")) break;

                System.out.print(" [*] Quantity: ");
                int bQty = Integer.parseInt(scanner.nextLine());

                Item item = itemDAO.getItemByCode(bItemCode);

                if (item != null) {
                    int availableInShelf = stockDAO.getShelfQuantity(item.getId());
                    if (availableInShelf <= 0) {
                        System.out.println("   [!] Shelf is empty!");
                    } else if (availableInShelf < bQty) {
                        System.out.println("   [!] Only " + availableInShelf + " items available on shelf!");
                    } else {
                        // DTO Pattern — Using BillItem instead of int[]
                        BillItem billItem = new BillItem(item.getId(), item.getCode(), item.getName(), bQty, item.getPrice());
                        bill.addItem(billItem);
                        System.out.println("   [OK] Added: " + item.getName() + " | Total: LKR " + billItem.getTotal() + "\n");
                    }
                } else {
                    System.out.println("   [!] Invalid Item Code.\n");
                }
            }

            if (bill.hasItems()) {
                // Decorator Pattern — Build pricing chain
                BillComponent pricing = new BasicBill(bill);

                // Ask if discount should be applied
                System.out.print("\n [?] Apply Discount? (yes/no): ");
                String discountChoice = scanner.nextLine().trim();
                if (discountChoice.equalsIgnoreCase("yes") || discountChoice.equalsIgnoreCase("y")) {
                    System.out.print(" [%] Enter Discount Percentage: ");
                    double discountPct = Double.parseDouble(scanner.nextLine());
                    pricing = new DiscountDecorator(pricing, discountPct);
                }

                // Ask if tax should be applied
                System.out.print(" [?] Apply Tax? (yes/no): ");
                String taxChoice = scanner.nextLine().trim();
                if (taxChoice.equalsIgnoreCase("yes") || taxChoice.equalsIgnoreCase("y")) {
                    System.out.print(" [%] Enter Tax Percentage: ");
                    double taxPct = Double.parseDouble(scanner.nextLine());
                    pricing = new TaxDecorator(pricing, taxPct);
                }

                // Calculate final total using Decorator chain
                double finalTotal = pricing.calculateTotal();
                bill.setTotalAmount(finalTotal);

                System.out.println("\n ------------------------------------------");
                System.out.println(" Pricing: " + pricing.getDescription());
                System.out.println(" [TOTAL] GRAND TOTAL: LKR " + String.format("%.2f", finalTotal));
                System.out.println(" ------------------------------------------");
                System.out.print(" [CASH] Cash Amount Received: LKR ");
                double cashReceived = Double.parseDouble(scanner.nextLine());

                bill.processPayment(cashReceived);

                if (bill.isPaymentSufficient()) {
                    int billId = billDAO.processBill(bill);

                    // Beautiful Receipt Preview
                    System.out.println("\n ══════════════════════════════════════════");
                    System.out.println("              SYOS OUTLET RECEIPT          ");
                    System.out.println(" ══════════════════════════════════════════");
                    System.out.println("  Bill #" + billId);
                    System.out.println("  Pricing : " + pricing.getDescription());
                    System.out.printf("  Total Bill Amount : LKR %10.2f\n", finalTotal);
                    System.out.printf("  Cash Tendered     : LKR %10.2f\n", cashReceived);
                    System.out.println(" ──────────────────────────────────────────");
                    System.out.printf("  BALANCE TO PAY    : LKR %10.2f\n", bill.getChangeAmount());
                    System.out.println(" ══════════════════════════════════════════");
                    System.out.println("          Thank You! Come Again.           ");
                    System.out.println(" ══════════════════════════════════════════\n");

                } else {
                    System.out.println("\n [!] Insufficient Cash! Transaction Cancelled.");
                }
            }
        } catch (Exception e) {
            System.out.println("\n [!] Error: " + e.getMessage());
        }
    }
}
