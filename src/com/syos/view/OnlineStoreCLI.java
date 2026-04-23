package com.syos.view;

import com.syos.dao.BillDAO;
import com.syos.dao.ItemDAO;
import com.syos.model.Item;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class OnlineStoreCLI {
    private Scanner scanner = new Scanner(System.in);
    private ItemDAO itemDAO = new ItemDAO();
    private BillDAO billDAO = new BillDAO();

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

    // ===== සියලුම Items Shelf Stock එක්ක පෙන්වීම =====
    private void viewItems() {
        try {
            List<String[]> items = itemDAO.getItemsWithShelfStock();

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
            for (String[] item : items) {
                int stockQty = Integer.parseInt(item[4]);
                if (stockQty > 0) {
                    hasItems = true;
                    
                    System.out.printf("║ %-4s ║ %-8s ║ %-28s ║ LKR %7s ║%n",
                        item[0], item[1], item[2], item[3]);
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

    // ===== Online Order Process කිරීම (Multi-Item Cart Support) =====
    private void processOnlineOrder() {
        try {
            List<int[]> cart = new ArrayList<>();
            List<String[]> cartDisplay = new ArrayList<>(); // For receipt display
            double totalAmount = 0;

            System.out.println("\n--- SYOS ONLINE ORDER ---");
            System.out.println("(Type 'done' when you have finished adding items)\n");

            // ===== Multi-item cart loop =====
            while (true) {
                System.out.print("Enter Item Code (or 'done' to finish): ");
                String code = scanner.nextLine().trim();

                if (code.equalsIgnoreCase("done")) {
                    if (cart.isEmpty()) {
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

                double lineTotal = item.getPrice() * qty;
                totalAmount += lineTotal;

                cart.add(new int[]{item.getId(), qty, (int) lineTotal});
                cartDisplay.add(new String[]{item.getName(), String.valueOf(qty), 
                    String.format("%.2f", item.getPrice()), String.format("%.2f", lineTotal)});

                System.out.println("Added to cart: " + item.getName() + " x" + qty + " = LKR " + String.format("%.2f", lineTotal));
                System.out.println("Cart Total: LKR " + String.format("%.2f", totalAmount));
                System.out.println();
            }

            // ===== Order Summary පෙන්වීම =====
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                     SYOS ONLINE ORDER SUMMARY                      ║");
            System.out.println("╠══════════════════════════════╦══════╦══════════════╦═══════════════╣");
            System.out.println("║          Item Name           ║ Qty  ║  Unit Price  ║  Line Total   ║");
            System.out.println("╠══════════════════════════════╬══════╬══════════════╬═══════════════╣");

            for (String[] row : cartDisplay) {
                System.out.printf("║ %-28s ║ %-4s ║ LKR %8s ║ LKR %9s ║%n",
                    row[0], row[1], row[2], row[3]);
            }

            System.out.println("╠══════════════════════════════╩══════╩══════════════╬═══════════════╣");
            System.out.printf("║ %50s ║ LKR %9s ║%n", "GRAND TOTAL", String.format("%.2f", totalAmount));
            System.out.println("╚════════════════════════════════════════════════════╩═══════════════╝");

            System.out.println("\nDelivery Type: HOME DELIVERY");
            System.out.println("Payment Method: CASH ON DELIVERY");

            // ===== Order Confirm කිරීම =====
            System.out.print("\nConfirm Order? (yes/no): ");
            String confirm = scanner.nextLine().trim();

            if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                // Bill Type එක 'ONLINE' ලෙස save කිරීම
                billDAO.processBill("ONLINE", totalAmount, totalAmount, 0, cart);

                System.out.println("\n╔═══════════════════════════════════════════════════════╗");
                System.out.println("║          ORDER PLACED SUCCESSFULLY!                   ║");
                System.out.println("╠═══════════════════════════════════════════════════════╣");
                System.out.println("║  Your items will be delivered to your                 ║");
                System.out.println("║  registered address.                                  ║");
                System.out.println("║                                                       ║");
                System.out.println("║  Payment: Cash on Delivery (COD)                      ║");
                System.out.printf("║  Total Amount: LKR %-35s║%n", String.format("%.2f", totalAmount));
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