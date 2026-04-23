package com.syos.factory;

import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import com.syos.gateway.BillGateway;

/**
 * Factory Pattern — Abstract factory interface for creating DAO instances.
 * Decouples client code from concrete DAO implementations,
 * enabling easy swapping (e.g., MySQL to PostgreSQL, or to in-memory for testing).
 */
public interface DAOFactory {
    ItemGateway createItemDAO();
    StockGateway createStockDAO();
    BillGateway createBillDAO();
}
