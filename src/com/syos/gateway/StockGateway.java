package com.syos.gateway;

import com.syos.model.Stock;
import com.syos.strategy.StockDepletionStrategy;
import java.sql.SQLException;

/**
 * Gateway Pattern — Interface abstracting Stock data access operations.
 * Decouples business logic from persistence implementation.
 */
public interface StockGateway {
    void addStock(Stock stock) throws SQLException;
    int getShelfQuantity(int itemId) throws SQLException;
    boolean reshelveItem(int itemId, int requestedQty, StockDepletionStrategy strategy) throws SQLException;
    void updateStockByCodeAndBatch(String itemCode, String batchNo, int quantity) throws SQLException;
}
