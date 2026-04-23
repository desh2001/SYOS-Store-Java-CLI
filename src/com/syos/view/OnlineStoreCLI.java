package com.syos.view;

import com.syos.factory.DAOFactory;
import com.syos.gateway.ItemGateway;
import com.syos.gateway.BillGateway;
import com.syos.model.Bill;
import com.syos.model.BillItem;
import com.syos.model.Item;
import com.syos.model.ItemStock;
import java.util.Scanner;
import java.util.List;

/**
 * Online Store CLI view.
 * 
 * Design Patterns used:
 * - Factory Pattern: receives DAOFactory for DAO creation
 * - Gateway Pattern: uses ItemGateway/BillGateway interfaces
 * - DTO Pattern: uses ItemStock, Bill, BillItem models instead of raw arrays
 */
public class OnlineStoreCLI {
    private Scanner scanner;
    private ItemGateway itemDAO;
    private BillGateway billDAO;

    // Factory Pattern — receives factory for DAO creation
    public OnlineStoreCLI(Scanner scanner, DAOFactory factory) {
        this.scanner = scanner;
        this.itemDAO = factory.createItemDAO();
        this.billDAO = factory.createBillDAO();
    }

    public void start() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     WELCOME TO SYOS ONLINE STORE         ║");
        System.out.println("╚══════════════════════════════════════════╝");

        while (true) {
            System.out.println("\n========= SYOS ONLINE STORE =========");
            System.out.println("1. View Available Items");
            System.out.println("2. Place an Order");
            System.out.println("3. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1, 2, or 3).");
                continue;
            }

            if (choice == 1) {
                viewItems();
            } else if (choice == 2) {
                processOnlineOrder();
            } else if (choice == 3) {
                System.out.println("Returning to Main Menu...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ===== DTO Pattern — Uses ItemStock instead of String[] =====
    private void viewItems() {
        try {
            List<ItemStock> items = itemDAO.getItemsWithShelfStock();

            if (items.isEmpty()) {
                System.out.println("\nNo items available in the store.");
                return;
            }

            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║             AVAILABLE ITEMS FOR ONLINE PURCHASE              ║");
            System.out.println("╠══════╦══════════╦══════════════════════════════╦═════════════╣");
            System.out.println("║  ID  ║   Code   ║          Item Name           ║    Price    ║");
            System.out.println("╠══════╬══════════╬══════════════════════════════╬═════════════╣");

            boolean hasItems = false;
            for (ItemStock item : items) {
                if (item.isAvailable()) {
                    hasItems = true;
                    System.out.printf("║ %-4d ║ %-8s ║ %-28s ║ LKR %7.2f ║%n",
                        item.getItemId(), item.getItemCode(), item.getItemName(), item.getUnitPrice());
                }
            }

            if (!hasItems) {
                System.out.println("║                 No items currently in stock                  ║");
            }

            System.out.println("╚══════╩══════════╩══════════════════════════════╩═════════════╝");
            System.out.println("TIP: Use the Item Code when placing an order.");

        } catch (Exception e) {
            System.out.println("Error loading items: " + e.getMessage());
        }
    }

    // ===== DTO Pattern — Uses Bill and BillItem instead of int[] and String[] =====
    private void processOnlineOrder() {
        try {
            // DTO Pattern — Using Bill object
            Bill bill = new Bill("ONLINE");

            System.out.println("\n--- SYOS ONLINE ORDER ---");
            System.out.println("(Type 'done' when you have finished adding items)\n");

            // ===== Multi-item cart loop =====
            while (true) {
                System.out.print("Enter Item Code (or 'done' to finish): ");
                String code = scanner.nextLine().trim();

                if (code.equalsIgnoreCase("done")) {
                    if (!bill.hasItems()) {
                        System.out.println("Your cart is empty. Order cancelled.");
                        return;
                    }
                    break;
                }

                // Item එක database එකෙන් සොයාගැනීම
                Item item = itemDAO.getItemByCode(code);

                if (item == null) {
                    System.out.println("Item not found with code: " + code + ". Try again.");
                    continue;
                }

                System.out.println("Found: " + item.getName() + " | Price: LKR " + String.format("%.2f", item.getPrice()));
                System.out.print("Enter Quantity: ");

                int qty;
                try {
                    qty = Integer.parseInt(scanner.nextLine().trim());
                    if (qty <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity. Please enter a number.");
                    continue;
                }

                // DTO Pattern — Using BillItem instead of int[]
                BillItem billItem = new BillItem(item.getId(), item.getCode(), item.getName(), qty, item.getPrice());
                bill.addItem(billItem);

                System.out.println("Added to cart: " + item.getName() + " x" + qty + " = LKR " + String.format("%.2f", billItem.getTotal()));
                System.out.println("Cart Total: LKR " + String.format("%.2f", bill.getTotalAmount()));
                System.out.println();
            }

            // ===== Order Summary =====
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                     SYOS ONLINE ORDER SUMMARY                      ║");
            System.out.println("╠══════════════════════════════╦══════╦══════════════╦═══════════════╣");
            System.out.println("║          Item Name           ║ Qty  ║  Unit Price  ║  Line Total   ║");
            System.out.println("╠══════════════════════════════╬══════╬══════════════╬═══════════════╣");

            for (BillItem item : bill.getItems()) {
                System.out.printf("║ %-28s ║ %-4d ║ LKR %8.2f ║ LKR %9.2f ║%n",
                    item.getName(), item.getQuantity(), item.getUnitPrice(), item.getTotal());
            }

            System.out.println("╠══════════════════════════════╩══════╩══════════════╬═══════════════╣");
            System.out.printf("║ %50s ║ LKR %9.2f ║%n", "GRAND TOTAL", bill.getTotalAmount());
            System.out.println("╚════════════════════════════════════════════════════╩═══════════════╝");

            System.out.println("\nDelivery Type: HOME DELIVERY");
            System.out.println("Payment Method: CASH ON DELIVERY");

            // ===== Order Confirm =====
            System.out.print("\nConfirm Order? (yes/no): ");
            String confirm = scanner.nextLine().trim();

            if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                // Bill Type එක 'ONLINE' ලෙස save කිරීම
                bill.processPayment(bill.getTotalAmount()); // COD — cash = total
                int billId = billDAO.processBill(bill);

                System.out.println("\n╔═══════════════════════════════════════════════════════╗");
                System.out.println("║          ORDER PLACED SUCCESSFULLY!                   ║");
                System.out.println("╠═══════════════════════════════════════════════════════╣");
                System.out.println("║  Order #" + String.format("%-45d", billId) + "║");
                System.out.println("║  Your items will be delivered to your                 ║");
                System.out.println("║  registered address.                                  ║");
                System.out.println("║                                                       ║");
                System.out.println("║  Payment: Cash on Delivery (COD)                      ║");
                System.out.printf("║  Total Amount: LKR %-35s║%n", String.format("%.2f", bill.getTotalAmount()));
                System.out.println("║                                                       ║");
                System.out.println("║  Thank you for shopping with SYOS!                    ║");
                System.out.println("╚═══════════════════════════════════════════════════════╝");
            } else {
                System.out.println("Order cancelled.");
            }

        } catch (Exception e) {
            System.out.println("Error processing order: " + e.getMessage());
        }
    }
}