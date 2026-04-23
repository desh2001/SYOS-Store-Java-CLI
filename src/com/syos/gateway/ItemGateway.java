package com.syos.gateway;

import com.syos.model.Item;
import com.syos.model.ItemStock;
import java.sql.SQLException;
import java.util.List;

/**
 * Gateway Pattern — Interface abstracting Item data access operations.
 * Decouples business logic from persistence implementation.
 */
public interface ItemGateway {
    void saveItem(Item item) throws SQLException;
    Item getItemByCode(String code) throws SQLException;
    List<Item> getAllItems() throws SQLException;
    List<ItemStock> getItemsWithShelfStock() throws SQLException;
    void updateItemByCode(String code, String name, double price) throws SQLException;
    void deleteItemByCode(String code) throws SQLException;
}
