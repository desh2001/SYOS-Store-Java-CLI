package com.syos.dao;

import com.syos.util.DatabaseConnection;
import java.sql.*;
import java.util.List;

public class BillDAO {

    // Bill එකක් Process කිරීම (Receipt save + Shelf stock අඩු කිරීම)
    public void processBill(String billType, double totalAmount, double cashTendered, double change, List<int[]> items) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            // 1. Bill එක bills table එකට save කිරීම
            String insertBill = "INSERT INTO bills (bill_type, total_amount, cash_paid, balance_amount, bill_date) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement billStmt = conn.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS);
            billStmt.setString(1, billType);
            billStmt.setDouble(2, totalAmount);
            billStmt.setDouble(3, cashTendered);
            billStmt.setDouble(4, change);
            billStmt.executeUpdate();

            // Auto-generated bill_id ලබාගැනීම
            ResultSet keys = billStmt.getGeneratedKeys();
            int billId = 0;
            if (keys.next()) {
                billId = keys.getInt(1);
            }

            // 2. Bill items එක එක save කිරීම + Shelf stock අඩු කිරීම
            for (int[] item : items) {
                int itemId = item[0];
                int qty = item[1];
                int lineTotal = item[2];

                // bill_items table එකට save
                String insertItem = "INSERT INTO bill_items (bill_id, item_id, quantity, item_total) VALUES (?, ?, ?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(insertItem);
                itemStmt.setInt(1, billId);
                itemStmt.setInt(2, itemId);
                itemStmt.setInt(3, qty);
                itemStmt.setInt(4, lineTotal);
                itemStmt.executeUpdate();

                // shelf_stock එකෙන් quantity අඩු කිරීම
                String updateShelf = "UPDATE shelf_stock SET quantity = quantity - ? WHERE item_id = ?";
                PreparedStatement shelfStmt = conn.prepareStatement(updateShelf);
                shelfStmt.setInt(1, qty);
                shelfStmt.setInt(2, itemId);
                shelfStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Bill #" + billId + " processed successfully!");

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
