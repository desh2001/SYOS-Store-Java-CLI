package com.syos.dao;

import com.syos.report.*;
import java.sql.SQLException;

public class ReportDAO {

    public void generateDailySalesReport() throws SQLException {
        new DailySalesReport().generate();
    }

    public void generateReshelvingReport() throws SQLException {
        new ReshelvingReport().generate();
    }

    public void generateReorderReport() throws SQLException {
        new ReorderReport().generate();
    }

    public void generateStockReport() throws SQLException {
        new StockReport().generate();
    }

    public void generateBillReport() throws SQLException {
        new BillReport().generate();
    }

    public void generateReport(int type) throws SQLException {
        switch (type) {
            case 1: generateDailySalesReport(); break;
            case 2: generateReshelvingReport(); break;
            case 3: generateReorderReport(); break;
            case 4: generateStockReport(); break;
            case 5: generateBillReport(); break;
            default: throw new IllegalArgumentException("Invalid report type: " + type);
        }
    }
}


