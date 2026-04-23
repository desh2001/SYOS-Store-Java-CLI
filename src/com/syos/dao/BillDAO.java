package com.syos.dao;

import com.syos.gateway.BillGateway;
import com.syos.model.Bill;
import com.syos.model.BillItem;
import com.syos.observer.StockSubject;
import com.syos.util.DatabaseConnection;
import java.sql.*;

/**
 * DAO Pattern + Gateway Pattern + Observer Pattern
 * Concrete implementation of BillGateway.
 * Processes bills using the Bill DTO and notifies observers of stock changes.
 */
public class BillDAO implements BillGateway {

    private StockSubject stockSubject;

    // Default constructor (no observers)
    public BillDAO() {
        this.stockSubject = new StockSubject();
    }

    // Constructor with observer support
    public BillDAO(StockSubject stockSubject) {
        this.stockSubject = stockSubject;
    }

    /**
     * Process a bill — saves bill + items, deducts shelf stock, notifies observers.
     * Uses Bill DTO instead of loose parameters (DTO Pattern).
     * Observer Pattern — notifies stock observers after each item's shelf stock is updated.
     * 
     * @param bill The Bill DTO containing all bill data
     * @return The generated bill ID
     */
    @Override
    public int processBill(Bill bill) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            // 1. Bill එක bills table එකට save කිරීම
            String insertBill = "INSERT INTO bills (bill_type, total_amount, cash_paid, balance_amount, bill_date) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement billStmt = conn.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS);
            billStmt.setString(1, bill.getBillType());
            billStmt.setDouble(2, bill.getTotalAmount());
            billStmt.setDouble(3, bill.getCashTendered());
            billStmt.setDouble(4, bill.getChangeAmount());
            billStmt.executeUpdate();

            // Auto-generated bill_id ලබාගැනීම
            ResultSet keys = billStmt.getGeneratedKeys();
            int billId = 0;
            if (keys.next()) {
                billId = keys.getInt(1);
            }

            // 2. Bill items එක එක save කිරීම + Shelf stock අඩු කිරීම
            for (BillItem item : bill.getItems()) {
                // bill_items table එකට save
                String insertItem = "INSERT INTO bill_items (bill_id, item_id, quantity, item_total) VALUES (?, ?, ?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(insertItem);
                itemStmt.setInt(1, billId);
                itemStmt.setInt(2, item.getItemId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getTotal());
                itemStmt.executeUpdate();

                // shelf_stock එකෙන් quantity අඩු කිරීම
                String updateShelf = "UPDATE shelf_stock SET quantity = quantity - ? WHERE item_id = ?";
                PreparedStatement shelfStmt = conn.prepareStatement(updateShelf);
                shelfStmt.setInt(1, item.getQuantity());
                shelfStmt.setInt(2, item.getItemId());
                shelfStmt.executeUpdate();
            }

            conn.commit();
            bill.setBillId(billId);

            // Observer Pattern — Notify observers of stock changes after successful commit
            for (BillItem item : bill.getItems()) {
                // Read updated shelf quantity
                String qtyQuery = "SELECT quantity FROM shelf_stock WHERE item_id = ?";
                PreparedStatement qtyStmt = conn.prepareStatement(qtyQuery);
                qtyStmt.setInt(1, item.getItemId());
                ResultSet qtyRs = qtyStmt.executeQuery();
                if (qtyRs.next()) {
                    int newQty = qtyRs.getInt("quantity");
                    stockSubject.notifyObservers(item.getItemId(), item.getName(), newQty);
                }
            }

            return billId;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
