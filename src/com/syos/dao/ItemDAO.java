package com.syos.dao;

import com.syos.model.Item;
import com.syos.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public void saveItem(Item item) throws SQLException {
        // Database එකට Item එකක් ඇතුළත් කරන Query එක
        String query = "INSERT INTO items (item_code, item_name, unit_price) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, item.getCode());
            statement.setString(2, item.getName());
            statement.setDouble(3, item.getPrice());
            
            statement.executeUpdate();
            System.out.println("Item added successfully: " + item.getName());
        }
    }

    // Item Code එකෙන් Item එක සොයාගැනීම (POS Billing සඳහා)
    public Item getItemByCode(String code) throws SQLException {
        String query = "SELECT * FROM items WHERE item_code = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, code);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    rs.getDouble("unit_price")
                );
            }
        }
        return null;
    }

    // සියලුම Items ලබාගැනීම
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM items";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    rs.getDouble("unit_price")
                ));
            }
        }
        return items;
    }

    // Shelf Stock එක්ක Items ලබාගැනීම (Online Store සඳහා)
    public List<String[]> getItemsWithShelfStock() throws SQLException {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT i.item_id, i.item_code, i.item_name, i.unit_price, COALESCE(s.quantity, 0) as stock_qty " +
                       "FROM items i LEFT JOIN shelf_stock s ON i.item_id = s.item_id";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                results.add(new String[]{
                    String.valueOf(rs.getInt("item_id")),
                    rs.getString("item_code"),
                    rs.getString("item_name"),
                    String.format("%.2f", rs.getDouble("unit_price")),
                    String.valueOf(rs.getInt("stock_qty"))
                });
            }
        }
        return results;
    }


    // Item Code එකෙන් Item එකක් Update කිරීම
    public void updateItemByCode(String code, String name, double price) throws SQLException {
        String query = "UPDATE items SET item_name = ?, unit_price = ? WHERE item_code = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, code);
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println(" [OK] Item updated successfully!");
            } else {
                System.out.println(" [!] Item not found with Code: " + code);
            }
        }
    }


    // Item Code එකෙන් Item එකක් Delete කිරීම
    public void deleteItemByCode(String code) throws SQLException {
        String query = "DELETE FROM items WHERE item_code = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, code); // 1 වෙනි පරාමිතියට Code එක දෙනවා
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println(" [OK] Item deleted successfully!");
            } else {
                System.out.println(" [!] Item not found with Code: " + code);
            }
        }
    }
    
}