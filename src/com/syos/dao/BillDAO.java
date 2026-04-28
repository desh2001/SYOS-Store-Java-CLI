package com.syos.dao;

import com.syos.gateway.BillGateway;
import com.syos.model.Bill;
import com.syos.model.BillItem;
import com.syos.observer.StockSubject;
import com.syos.util.DatabaseConnection;
import java.sql.*;

public class BillDAO implements BillGateway {

    private StockSubject stockSubject;

    public BillDAO() {
        this.stockSubject = new StockSubject();
    }

    public BillDAO(StockSubject stockSubject) {
        this.stockSubject = stockSubject;
    }

    @Override
    public int processBill(Bill bill) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {

            String insertBill = "INSERT INTO bills (bill_type, total_amount, cash_paid, balance_amount, bill_date) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement billStmt = conn.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS);
            billStmt.setString(1, bill.getBillType());
            billStmt.setDouble(2, bill.getTotalAmount());
            billStmt.setDouble(3, bill.getCashTendered());
            billStmt.setDouble(4, bill.getChangeAmount());
            billStmt.executeUpdate();

            ResultSet keys = billStmt.getGeneratedKeys();
            int billId = 0;
            if (keys.next()) {
                billId = keys.getInt(1);
            }

            for (BillItem item : bill.getItems()) {

                String insertItem = "INSERT INTO bill_items (bill_id, item_id, quantity, item_total) VALUES (?, ?, ?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(insertItem);
                itemStmt.setInt(1, billId);
                itemStmt.setInt(2, item.getItemId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getTotal());
                itemStmt.executeUpdate();

                String updateShelf = "UPDATE shelf_stock SET quantity = quantity - ? WHERE item_id = ?";
                PreparedStatement shelfStmt = conn.prepareStatement(updateShelf);
                shelfStmt.setInt(1, item.getQuantity());
                shelfStmt.setInt(2, item.getItemId());
                shelfStmt.executeUpdate();
            }

            conn.commit();
            bill.setBillId(billId);

            if (!bill.getBillType().equalsIgnoreCase("ONLINE")) {
                for (BillItem item : bill.getItems()) {

                    String qtyQuery = "SELECT quantity FROM shelf_stock WHERE item_id = ?";
                    PreparedStatement qtyStmt = conn.prepareStatement(qtyQuery);
                    qtyStmt.setInt(1, item.getItemId());
                    ResultSet qtyRs = qtyStmt.executeQuery();
                    if (qtyRs.next()) {
                        int newQty = qtyRs.getInt("quantity");
                        stockSubject.notifyObservers(item.getItemId(), item.getName(), newQty);
                    }
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


