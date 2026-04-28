package com.syos.dao;

import com.syos.gateway.StockGateway;
import com.syos.model.Stock;
import com.syos.strategy.StockDepletionStrategy;
import com.syos.strategy.FEFOStrategy;
import com.syos.util.DatabaseConnection;
import java.sql.*;

public class StockDAO implements StockGateway {

    @Override
    public void addStock(Stock stock) throws SQLException {
        String query = "INSERT INTO stock_store (item_id, batch_no, quantity, expiry_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, stock.getItemId());
            stmt.setString(2, stock.getBatchNo());
            stmt.setInt(3, stock.getQuantity());
            stmt.setDate(4, new java.sql.Date(stock.getExpiryDate().getTime()));

            stmt.executeUpdate();
        }
    }

    @Override
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
        return 0;
    }

    @Override
    public boolean reshelveItem(int itemId, int requestedQty, StockDepletionStrategy strategy) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        conn.setAutoCommit(false);

        try {

            String selectQuery = "SELECT stock_id, quantity FROM stock_store " +
                                 "WHERE item_id = ? AND quantity > 0 " +
                                 strategy.getOrderByClause();

            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, itemId);
            ResultSet rs = selectStmt.executeQuery();

            int remainingToMove = requestedQty;

            while (rs.next() && remainingToMove > 0) {
                int stockId = rs.getInt("stock_id");
                int availableQty = rs.getInt("quantity");

                int amountToTake = Math.min(availableQty, remainingToMove);

                String updateStockQuery = "UPDATE stock_store SET quantity = quantity - ? WHERE stock_id = ?";
                PreparedStatement updateStockStmt = conn.prepareStatement(updateStockQuery);
                updateStockStmt.setInt(1, amountToTake);
                updateStockStmt.setInt(2, stockId);
                updateStockStmt.executeUpdate();

                remainingToMove -= amountToTake;
            }

            if (remainingToMove == 0) {

                String updateShelfQuery = "INSERT INTO shelf_stock (item_id, quantity) VALUES (?, ?) " +
                                          "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
                PreparedStatement updateShelfStmt = conn.prepareStatement(updateShelfQuery);
                updateShelfStmt.setInt(1, itemId);
                updateShelfStmt.setInt(2, requestedQty);
                updateShelfStmt.setInt(3, requestedQty);
                updateShelfStmt.executeUpdate();

                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public void updateStockByCodeAndBatch(String itemCode, String batchNo, int quantity) throws SQLException {

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


