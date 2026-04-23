package com.syos.report;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Template Method Pattern — Concrete report: Reshelving Report.
 * Shows items with shelf stock below 20 that need refilling.
 */
public class ReshelvingReport extends ReportTemplate {

    @Override
    protected String getTitle() {
        return "RESHELVING REPORT (Items to Refill on Shelf)";
    }

    @Override
    protected String getQuery() {
        return "SELECT i.item_code, i.item_name, s.quantity as total_qty, 'NEED REFILL' as status " +
               "FROM shelf_stock s JOIN items i ON s.item_id = i.item_id " +
               "WHERE s.quantity < 20";
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"Code", "Item Name", "Current Qty", "Status"};
    }

    @Override
    protected String formatRow(ResultSet rs) throws SQLException {
        return String.format("│ %-10s │ %-30.30s │ %-15s │ %-15s │",
            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
    }
}
