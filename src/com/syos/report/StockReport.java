package com.syos.report;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Template Method Pattern — Concrete report: Warehouse Stock Report.
 * Shows batch-wise stock details with expiry dates.
 * Overrides printHeader() and printFooter() for a 5-column layout.
 */
public class StockReport extends ReportTemplate {

    @Override
    protected String getTitle() {
        return "WAREHOUSE STOCK REPORT (BATCH WISE)";
    }

    @Override
    protected String getQuery() {
        return "SELECT i.item_code, i.item_name, st.batch_no, st.quantity, st.expiry_date " +
               "FROM stock_store st JOIN items i ON st.item_id = i.item_id " +
               "ORDER BY st.expiry_date ASC";
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"Code", "Item Name", "Batch No", "Qty", "Expiry Date"};
    }

    @Override
    protected String formatRow(ResultSet rs) throws SQLException {
        return String.format("│ %-10s │ %-30s │ %-12s │ %-6d │ %-12s │",
            rs.getString("item_code"),
            rs.getString("item_name"),
            rs.getString("batch_no"),
            rs.getInt("quantity"),
            rs.getString("expiry_date"));
    }

    // Override header for 5-column layout
    @Override
    protected void printHeader() {
        String[] headers = getHeaders();
        System.out.println("┌────────────┬────────────────────────────────┬──────────────┬────────┬──────────────┐");
        System.out.printf ("│ %-10s │ %-30s │ %-12s │ %-6s │ %-12s │\n",
            headers[0], headers[1], headers[2], headers[3], headers[4]);
        System.out.println("├────────────┼────────────────────────────────┼──────────────┼────────┼──────────────┤");
    }

    // Override footer for 5-column layout
    @Override
    protected void printFooter() {
        System.out.println("└────────────┴────────────────────────────────┴──────────────┴────────┴──────────────┘");
    }
}
