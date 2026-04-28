package com.syos.report;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReorderReport extends ReportTemplate {

    @Override
    protected String getTitle() {
        return "REORDER LEVEL REPORT (Total Stock < 50)";
    }

    @Override
    protected String getQuery() {
        return "SELECT i.item_code, i.item_name, " +
               "(COALESCE((SELECT SUM(quantity) FROM stock_store WHERE item_id = i.item_id), 0) + " +
               "COALESCE((SELECT quantity FROM shelf_stock WHERE item_id = i.item_id), 0)) as total_qty, " +
               "'REORDER' as status " +
               "FROM items i HAVING total_qty < 50";
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"Code", "Item Name", "Total Stock", "Action Required"};
    }

    @Override
    protected String formatRow(ResultSet rs) throws SQLException {
        return String.format("│ %-10s │ %-30.30s │ %-15s │ %-15s │",
            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
    }
}


