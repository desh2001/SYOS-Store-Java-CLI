package com.syos.report;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Template Method Pattern — Concrete report: Daily Sales Report.
 * Shows items sold today with quantities and revenue.
 */
public class DailySalesReport extends ReportTemplate {

    @Override
    protected String getTitle() {
        return "DAILY SALES REPORT";
    }

    @Override
    protected String getQuery() {
        return "SELECT i.item_code, i.item_name, SUM(bi.quantity) as total_qty, SUM(bi.item_total) as total_revenue " +
               "FROM bill_items bi JOIN bills b ON bi.bill_id = b.bill_id " +
               "JOIN items i ON bi.item_id = i.item_id " +
               "WHERE DATE(b.bill_date) = CURRENT_DATE GROUP BY i.item_id";
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"Code", "Item Name", "Total Qty", "Total Revenue"};
    }

    @Override
    protected String formatRow(ResultSet rs) throws SQLException {
        return String.format("│ %-10s │ %-30.30s │ %-15s │ %-15s │",
            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
    }
}
