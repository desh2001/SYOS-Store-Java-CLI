package com.syos.factory;

import com.syos.dao.ItemDAO;
import com.syos.dao.StockDAO;
import com.syos.dao.BillDAO;
import com.syos.gateway.ItemGateway;
import com.syos.gateway.StockGateway;
import com.syos.gateway.BillGateway;
import com.syos.observer.StockSubject;
import com.syos.observer.ReorderLevelObserver;
import com.syos.observer.LowStockObserver;

/**
 * Factory Pattern + Singleton Pattern — Concrete factory for MySQL-backed DAOs.
 * Creates DAO instances pre-configured with observers.
 * Uses Singleton to provide a single factory instance across the application.
 */
public class MySQLDAOFactory implements DAOFactory {

    private static MySQLDAOFactory instance;
    private StockSubject stockSubject;

    private MySQLDAOFactory() {
        // Observer Pattern — Set up stock observers once in the factory
        stockSubject = new StockSubject();
        stockSubject.addObserver(new ReorderLevelObserver());
        stockSubject.addObserver(new LowStockObserver());
    }

    public static MySQLDAOFactory getInstance() {
        if (instance == null) {
            instance = new MySQLDAOFactory();
        }
        return instance;
    }

    @Override
    public ItemGateway createItemDAO() {
        return new ItemDAO();
    }

    @Override
    public StockGateway createStockDAO() {
        return new StockDAO();
    }

    @Override
    public BillGateway createBillDAO() {
        // BillDAO gets the shared stock subject for observer notifications
        return new BillDAO(stockSubject);
    }

    // Expose the stock subject for external observer registration
    public StockSubject getStockSubject() {
        return stockSubject;
    }
}
