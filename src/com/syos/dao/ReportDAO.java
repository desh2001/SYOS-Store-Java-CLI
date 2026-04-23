package com.syos.dao;

import com.syos.report.*;
import java.sql.SQLException;

/**
 * DAO Pattern — Report data access, now delegates to Template Method Pattern classes.
 * Maintains backward compatibility — existing callers still use the same method names.
 */
public class ReportDAO {

    // 1. දෛනික අලෙවි වාර්තාව (Daily Sales Report) — delegates to Template Method
    public void generateDailySalesReport() throws SQLException {
        new DailySalesReport().generate();
    }

    // 2. නැවත රාක්කගත කිරීමේ වාර්තාව (Reshelving Report) — delegates to Template Method
    public void generateReshelvingReport() throws SQLException {
        new ReshelvingReport().generate();
    }

    // 3. ඇණවුම් මට්ටම් (Reorder Level Report) — delegates to Template Method
    public void generateReorderReport() throws SQLException {
        new ReorderReport().generate();
    }

    // 4. තොග වාර්තාව (Stock Report) — delegates to Template Method
    public void generateStockReport() throws SQLException {
        new StockReport().generate();
    }

    // 5. බිල්පත් වාර්තාව (Bill Report) — delegates to Template Method
    public void generateBillReport() throws SQLException {
        new BillReport().generate();
    }
}