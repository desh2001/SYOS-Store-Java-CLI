package com.syos.dao;

import com.syos.model.Stock;
import com.syos.util.DatabaseConnection;
import java.sql.*;

public class StockDAO {
    
    // ගබඩාවට (Stock Store) අලුත් තොගයක් ඇතුළත් කිරීම
    public void addStock(Stock stock) throws SQLException {
        String query = "INSERT INTO stock_store (item_id, batch_no, quantity, expiry_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, stock.getItemId());
            stmt.setString(2, stock.getBatchNo());
            stmt.setInt(3, stock.getQuantity());
            stmt.setDate(4, new java.sql.Date(stock.getExpiryDate().getTime()));
            
            stmt.executeUpdate();
            System.out.println("Stock added successfully!");
        }
    }

    public int getShelfQuantity(int itemId) throws SQLException {
        String query = "SELECT quantity FROM shelf_stock WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        }
        return 0; // Item එක shelf එකේ නැත්නම් 0 පෙන්වයි
    }

    public void reshelveItem(int itemId, int requestedQty) throws SQLException {
    Connection conn = DatabaseConnection.getInstance().getConnection();
    // Transaction එකක් ආරම්භ කිරීම (Data safe තබා ගැනීමට)
    conn.setAutoCommit(false);

    try {
        // 1. FEFO අනුව අදාළ Item එකේ Batches ලබා ගැනීම
        String selectQuery = "SELECT stock_id, quantity FROM stock_store " +
                             "WHERE item_id = ? AND quantity > 0 " +
                             "ORDER BY expiry_date ASC";
        
        PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
        selectStmt.setInt(1, itemId);
        ResultSet rs = selectStmt.executeQuery();

        int remainingToMove = requestedQty;

        while (rs.next() && remainingToMove > 0) {
            int stockId = rs.getInt("stock_id");
            int availableQty = rs.getInt("quantity");

            int amountToTake = Math.min(availableQty, remainingToMove);

            // 2. stock_store එකෙන් ප්‍රමාණය අඩු කිරීම
            String updateStockQuery = "UPDATE stock_store SET quantity = quantity - ? WHERE stock_id = ?";
            PreparedStatement updateStockStmt = conn.prepareStatement(updateStockQuery);
            updateStockStmt.setInt(1, amountToTake);
            updateStockStmt.setInt(2, stockId);
            updateStockStmt.executeUpdate();

            remainingToMove -= amountToTake;
        }

        if (remainingToMove == 0) {
            // 3. shelf_stock එකට ප්‍රමාණය එකතු කිරීම (UPSERT logic)
            String updateShelfQuery = "INSERT INTO shelf_stock (item_id, quantity) VALUES (?, ?) " +
                                      "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
            PreparedStatement updateShelfStmt = conn.prepareStatement(updateShelfQuery);
            updateShelfStmt.setInt(1, itemId);
            updateShelfStmt.setInt(2, requestedQty);
            updateShelfStmt.setInt(3, requestedQty);
            updateShelfStmt.executeUpdate();

            conn.commit(); // ඔක්කොම හරි නම් Save කරනවා
            System.out.println("Suceesful");
        } else {
            conn.rollback(); // බඩු මදි නම් කරපු වෙනස්කම් අවලංගු කරනවා
            System.out.println("Not enough stock");
        }
        

    } catch (SQLException e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
    }
    }

    // Item Code සහ Batch No මගින් Warehouse තොගය Update කිරීම
    public void updateStockByCodeAndBatch(String itemCode, String batchNo, int quantity) throws SQLException {
        // JOIN එකක් පාවිච්චි කරලා items table එකෙන් code එක හොයාගෙන stock_store එක update කරනවා
        String query = "UPDATE stock_store ss JOIN items i ON ss.item_id = i.item_id " +
                       "SET ss.quantity = ? WHERE i.item_code = ? AND ss.batch_no = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, quantity);
            stmt.setString(2, itemCode);
            stmt.setString(3, batchNo);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" [OK] Warehouse stock updated successfully!");
            } else {
                System.out.println(" [!] Stock not found for Code: " + itemCode + " and Batch: " + batchNo);
            }
        }
    }

}