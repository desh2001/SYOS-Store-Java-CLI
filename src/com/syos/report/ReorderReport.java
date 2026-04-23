package com.syos.report;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Template Method Pattern — Concrete report: Reorder Level Report.
 * Shows items with total warehouse stock below 50 that need reordering.
 */
public class ReorderReport extends ReportTemplate {

    @Override
    protected String getTitle() {
        return "REORDER LEVEL REPORT (Total Stock < 50)";
    }

    @Override
    protected String getQuery() {
        return "SELECT i.item_code, i.item_name, SUM(st.quantity) as total_qty, 'REORDER' as status " +
               "FROM stock_store st JOIN items i ON st.item_id = i.item_id " +
               "GROUP BY i.item_id HAVING total_qty < 50";
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"Code", "Item Name", "Warehouse Qty", "Action Required"};
    }

    @Override
    protected String formatRow(ResultSet rs) throws SQLException {
        return String.format("│ %-10s │ %-30.30s │ %-15s │ %-15s │",
            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
    }
}
