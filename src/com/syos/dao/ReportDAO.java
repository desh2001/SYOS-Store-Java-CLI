package com.syos.dao;

import com.syos.util.DatabaseConnection;
import java.sql.*;

public class ReportDAO {

    // 1. දෛනික අලෙවි වාර්තාව (Daily Sales Report)
    public void generateDailySalesReport() throws SQLException {
        String query = "SELECT i.item_code, i.item_name, SUM(bi.quantity) as total_qty, SUM(bi.item_total) as total_revenue " +
                       "FROM bill_items bi JOIN bills b ON bi.bill_id = b.bill_id " +
                       "JOIN items i ON bi.item_id = i.item_id " +
                       "WHERE DATE(b.bill_date) = CURRENT_DATE GROUP BY i.item_id";

        System.out.println("\n --- DAILY SALES REPORT ---");
        printReport(query, "Code", "Item Name", "Total Qty", "Total Revenue");
    }

    // 2. නැවත රාක්කගත කිරීමේ වාර්තාව (Reshelving Report - Shelf < 20 threshold)
    public void generateReshelvingReport() throws SQLException {
        String query = "SELECT i.item_code, i.item_name, s.quantity as total_qty, 'NEED REFILL' as status " +
                       "FROM shelf_stock s JOIN items i ON s.item_id = i.item_id " +
                       "WHERE s.quantity < 20";

        System.out.println("\n --- RESHELVING REPORT (Items to Refill on Shelf) ---");
        printReport(query, "Code", "Item Name", "Current Qty", "Status");
    }

    // 3. ඇණවුම් මට්ටම් (Reorder Level Report - Store < 50)
    public void generateReorderReport() throws SQLException {
        String query = "SELECT i.item_code, i.item_name, SUM(st.quantity) as total_qty, 'REORDER' as status " +
                       "FROM stock_store st JOIN items i ON st.item_id = i.item_id " +
                       "GROUP BY i.item_id HAVING total_qty < 50";

        System.out.println("\n --- REORDER LEVEL REPORT (Total Stock < 50) ---");
        printReport(query, "Code", "Item Name", "Warehouse Qty", "Action Required");
    }

    // 4. තොග වාර්තාව (Stock Report - Batches details)
    public void generateStockReport() throws SQLException {
        String query = "SELECT i.item_code, i.item_name, st.batch_no, st.quantity, st.expiry_date " +
                       "FROM stock_store st JOIN items i ON st.item_id = i.item_id " +
                       "ORDER BY st.expiry_date ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\n --- WAREHOUSE STOCK REPORT (BATCH WISE) ---");
            System.out.println("┌────────────┬────────────────────────────────┬──────────────┬────────┬──────────────┐");
            System.out.printf ("│ %-10s │ %-30s │ %-12s │ %-6s │ %-12s │\n", "Code", "Item Name", "Batch No", "Qty", "Expiry Date");
            System.out.println("├────────────┼────────────────────────────────┼──────────────┼────────┼──────────────┤");
            
            while (rs.next()) {
                System.out.printf("│ %-10s │ %-30s │ %-12s │ %-6d │ %-12s │\n", 
                    rs.getString("item_code"), 
                    rs.getString("item_name"), 
                    rs.getString("batch_no"), 
                    rs.getInt("quantity"), 
                    rs.getString("expiry_date"));
            }
            System.out.println("└────────────┴────────────────────────────────┴──────────────┴────────┴──────────────┘");
        }
    }

    // 5. බිල්පත් වාර්තාව (Bill Report - All Transactions)
    public void generateBillReport() throws SQLException {
        String query = "SELECT bill_id, bill_date, bill_type, total_amount, cash_paid FROM bills ORDER BY bill_id DESC";
        
        System.out.println("\n --- ALL TRANSACTIONS REPORT ---");
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("┌──────────┬───────────────────────┬────────────┬────────────────┬────────────────┐");
            System.out.printf ("│ %-8s │ %-21s │ %-10s │ %-14s │ %-14s │\n", "Bill ID", "Date & Time", "Type", "Total (LKR)", "Cash (LKR)");
            System.out.println("├──────────┼───────────────────────┼────────────┼────────────────┼────────────────┤");
            
            while (rs.next()) {
                // දිනය සහ වෙලාව පෙන්වන්න rs.getString පාවිච්චි කරලා තියෙනවා
                System.out.printf("│ %-8d │ %-21.21s │ %-10s │ %-14.2f │ %-14.2f │\n", 
                    rs.getInt("bill_id"), 
                    rs.getString("bill_date"), 
                    rs.getString("bill_type"), 
                    rs.getDouble("total_amount"), 
                    rs.getDouble("cash_paid"));
            }
            System.out.println("└──────────┴───────────────────────┴────────────┴────────────────┴────────────────┘");
        }
    }

    // පොදු පින්තාරු කිරීමේ method එක (1, 2 සහ 3 Reports වලට අදාලව)
    private void printReport(String query, String h1, String h2, String h3, String h4) throws SQLException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("┌────────────┬────────────────────────────────┬─────────────────┬─────────────────┐");
            System.out.printf ("│ %-10s │ %-30s │ %-15s │ %-15s │\n", h1, h2, h3, h4);
            System.out.println("├────────────┼────────────────────────────────┼─────────────────┼─────────────────┤");
            
            while (rs.next()) {
                // Item Name එක අකුරු 30කට සීමා කරලා තියෙන්නේ (අකුරු 30ට වඩා දිග නම් කැපිලා යයි, හැබැයි box එක කැඩෙන්නේ නෑ)
                System.out.printf("│ %-10s │ %-30.30s │ %-15s │ %-15s │\n", 
                    rs.getString(1), 
                    rs.getString(2), 
                    rs.getString(3), 
                    rs.getString(4));
            }
            System.out.println("└────────────┴────────────────────────────────┴─────────────────┴─────────────────┘");
        }
    }
}