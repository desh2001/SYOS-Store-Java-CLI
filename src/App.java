import com.syos.dao.ItemDAO;
import com.syos.dao.ReportDAO;
import com.syos.dao.StockDAO;
import com.syos.dao.BillDAO;
import com.syos.model.Item;
import com.syos.view.OnlineStoreCLI;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n\n");
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║         SYOS SYSTEM ADMIN LOGIN          ║");
            System.out.println("╚══════════════════════════════════════════╝");
      
            boolean isAuthenticated = false;
            while (!isAuthenticated) {
                System.out.print(" [>] Enter Username: ");
                String username = scanner.nextLine().trim();
          
                System.out.print(" [>] Enter Password: ");
                String password = scanner.nextLine().trim();
          
                if (username.equals("admin") && password.equals("admin123")) {
                    isAuthenticated = true;
                    System.out.println("\n [OK] Login successful! Welcome, " + username + ".");
                } else {
                    System.out.println(" [!] Invalid username or password. Please try again.\n");
                }
            }

            boolean isMenuActive = true;
            while(isMenuActive) {

                System.out.println("\n╔══════════════════════════════════════════╗");
                System.out.println("║           WELCOME TO SYOS MENU           ║");
                System.out.println("╠══════════════════════════════════════════╣");
                System.out.println("║  1. Inventory Management (Items & Stock) ║");
                System.out.println("║  2. Create Bill (POS)                    ║");
                System.out.println("║  3. Online Store                         ║");
                System.out.println("║  4. Reports                              ║");
                System.out.println("║  5. Logout                               ║");
                System.out.println("╚══════════════════════════════════════════╝");
                System.out.print(" >>> Select an option (1-5): ");
            
                int choice = -1;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println(" [!] Please enter a valid number.");
                    continue;
                }

                try {
                    if (choice == 1) {
                        // --- INVENTORY MANAGEMENT SUB-MENU ---
                        System.out.println("\n┌──────────────────────────────────────────┐");
                        System.out.println("│           INVENTORY MANAGEMENT           │");
                        System.out.println("├──────────────────────────────────────────┤");
                        System.out.println("│  1. Add New Item                         │");
                        System.out.println("│  2. Update Item Details                  │");
                        System.out.println("│  3. Delete Item                          │");
                        System.out.println("│  4. Add Stock to Warehouse               │");
                        System.out.println("│  5. Update Warehouse Stock Qty           │");
                        System.out.println("│  6. Stock Transfer (Warehouse to Shelf)  │");
                        System.out.println("│  7. Back to Main Menu                    │");
                        System.out.println("└──────────────────────────────────────────┘");
                        System.out.print(" >>> Select Sub-option (1-7): ");
                        
                        int invChoice = Integer.parseInt(scanner.nextLine());

                        ItemDAO itemDAO = new ItemDAO();
                        StockDAO stockDAO = new StockDAO();

                        System.out.println();
                        switch (invChoice) {
                            case 1: 
                                System.out.print(" [+] Item Code: "); String code = scanner.nextLine();
                                System.out.print(" [+] Item Name: "); String name = scanner.nextLine();
                                System.out.print(" [+] Price (LKR): "); double price = Double.parseDouble(scanner.nextLine());
                                itemDAO.saveItem(new Item(code, name, price));
                                break;
                            case 2: 
                                System.out.print(" [~] Enter Item Code to Update: "); 
                                String uCode = scanner.nextLine().trim();
                                System.out.print(" [~] New Name: "); 
                                String uName = scanner.nextLine();
                                System.out.print(" [~] New Price (LKR): "); 
                                double uPrice = Double.parseDouble(scanner.nextLine());
                                itemDAO.updateItemByCode(uCode, uName, uPrice); 
                                break;
                            case 3: 
                                System.out.print(" [-] Enter Item Code to Delete: "); 
                                String dCode = scanner.nextLine().trim();
                                System.out.print(" [?] Are you sure? (yes/no): ");
                                if(scanner.nextLine().equalsIgnoreCase("yes")) {
                                    itemDAO.deleteItemByCode(dCode);
                                } else {
                                    System.out.println(" [!] Deletion cancelled.");
                                }
                                break;
                            case 4: 
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
                                    stockDAO.addStock(new com.syos.model.Stock(addItem.getId(), batchNo, qty, expiry));
                                } else {
                                    System.out.println(" [!] Item not found with Code: " + addCode);
                                }
                                break;

                            case 5: 
                                // Stock එක Update කරන්න Item Code එකයි, අදාළ Batch No එකයි ඉල්ලනවා
                                System.out.print(" [~] Enter Item Code: "); String uStockCode = scanner.nextLine().trim();
                                System.out.print(" [~] Enter Batch No: "); String uBatch = scanner.nextLine().trim();
                                System.out.print(" [~] New Quantity: "); int sQty = Integer.parseInt(scanner.nextLine());
                                
                                // මේක අලුත් method එකක්, අපි StockDAO එකට එකතු කරන්න ඕනේ
                                stockDAO.updateStockByCodeAndBatch(uStockCode, uBatch, sQty);
                                break;

                            case 6: 
                                System.out.print(" [>] Enter Item Code to Reshelve: "); 
                                String resCode = scanner.nextLine().trim();
                                
                                // Code එකෙන් Item එක හොයාගන්නවා
                                Item resItem = itemDAO.getItemByCode(resCode);
                                
                                if (resItem != null) {
                                    System.out.print(" [>] Enter Quantity to move to Shelf: ");
                                    int reshelveQty = Integer.parseInt(scanner.nextLine());
                                    
                                    // හොයාගත්ත Item එකේ ID එක මෙතනින් pass කරනවා
                                    stockDAO.reshelveItem(resItem.getId(), reshelveQty);
                                } else {
                                    System.out.println(" [!] Item not found with Code: " + resCode);
                                }
                                break;

                            case 7: 
                                System.out.println(" [<] Returning to Main Menu...");
                                break;

                            default:
                                System.out.println(" [!] Invalid Selection.");
                        }
                    } 
                    else if (choice == 2) {
                        // --- BILLING PROCESS (POS) ---
                        List<int[]> billItems = new ArrayList<>();
                        double grandTotal = 0;

                        System.out.println("\n --- ADD ITEMS TO BILL ---");
                        System.out.println(" (Enter 'done' or '0' to Finish)\n");
                        
                        while (true) {
                            System.out.print(" [*] Enter Item Code: ");
                            String bItemCode = scanner.nextLine().trim();
                            if (bItemCode.equalsIgnoreCase("done") || bItemCode.equals("0")) break;

                            System.out.print(" [*] Quantity: ");
                            int bQty = Integer.parseInt(scanner.nextLine());

                            Item item = new ItemDAO().getItemByCode(bItemCode); 
                            
                            if (item != null) {
                                int availableInShelf = new StockDAO().getShelfQuantity(item.getId()); 
                                if (availableInShelf <= 0) {
                                    System.out.println("   [!] Shelf is empty!");
                                } else if (availableInShelf < bQty) {
                                    System.out.println("   [!] Only " + availableInShelf + " items available on shelf!");
                                } else {
                                    double itemTotal = item.getPrice() * bQty;
                                    grandTotal += itemTotal;
                                    billItems.add(new int[]{item.getId(), bQty, (int)itemTotal});
                                    System.out.println("   [OK] Added: " + item.getName() + " | Total: LKR " + itemTotal + "\n");
                                }
                            } else {
                                System.out.println("   [!] Invalid Item Code.\n");
                            }
                        }

                        if (!billItems.isEmpty()) {
                            System.out.println("\n ------------------------------------------");
                            System.out.println(" [TOTAL] GRAND TOTAL: LKR " + String.format("%.2f", grandTotal));
                            System.out.println(" ------------------------------------------");
                            System.out.print(" [CASH] Cash Amount Received: LKR ");
                            double cashReceived = Double.parseDouble(scanner.nextLine());

                            if (cashReceived >= grandTotal) {
                                double balanceAmount = cashReceived - grandTotal;
                                new BillDAO().processBill("OFFLINE", grandTotal, cashReceived, balanceAmount, billItems);
                                
                                // Beautiful Receipt Preview
                                System.out.println("\n ══════════════════════════════════════════");
                                System.out.println("              SYOS OUTLET RECEIPT          ");
                                System.out.println(" ══════════════════════════════════════════");
                                System.out.printf("  Total Bill Amount : LKR %10.2f\n", grandTotal);
                                System.out.printf("  Cash Tendered     : LKR %10.2f\n", cashReceived);
                                System.out.println(" ──────────────────────────────────────────");
                                System.out.printf("  BALANCE TO PAY    : LKR %10.2f\n", balanceAmount);
                                System.out.println(" ══════════════════════════════════════════");
                                System.out.println("          Thank You! Come Again.           ");
                                System.out.println(" ══════════════════════════════════════════\n");
                                
                            } else {
                                System.out.println("\n [!] Insufficient Cash! Transaction Cancelled.");
                            }
                        }
                    }
                    else if (choice == 3) {
                        System.out.println("\n [*] Launching Online Store CLI...");
                        new OnlineStoreCLI().start();
                    }
                    else if (choice == 4) {
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
                    }
                    else if (choice == 5) {
                        System.out.println("\n [*] Logging out...\n");
                        isMenuActive = false;
                       
                    } else {
                        System.out.println(" [!] Invalid choice. Please select from 1-5.");
                    }
                } catch (Exception e) {
                    System.out.println("\n [!] Error: " + e.getMessage());
                }
            }
        }
    }
}