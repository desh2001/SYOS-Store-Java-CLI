package com.syos.report;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Template Method Pattern — Concrete report: All Transactions Report.
 * Shows all bills with date, type, total, and cash paid.
 * Overrides printHeader() and printFooter() for a 5-column layout.
 */
public class BillReport extends ReportTemplate {

    @Override
    protected String getTitle() {
        return "ALL TRANSACTIONS REPORT";
    }

    @Override
    protected String getQuery() {
        return "SELECT bill_id, bill_date, bill_type, total_amount, cash_paid FROM bills ORDER BY bill_id DESC";
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"Bill ID", "Date & Time", "Type", "Total (LKR)", "Cash (LKR)"};
    }

    @Override
    protected String formatRow(ResultSet rs) throws SQLException {
        return String.format("│ %-8d │ %-21.21s │ %-10s │ %-14.2f │ %-14.2f │",
            rs.getInt("bill_id"),
            rs.getString("bill_date"),
            rs.getString("bill_type"),
            rs.getDouble("total_amount"),
            rs.getDouble("cash_paid"));
    }

    // Override header for 5-column transaction layout
    @Override
    protected void printHeader() {
        String[] headers = getHeaders();
        System.out.println("┌──────────┬───────────────────────┬────────────┬────────────────┬────────────────┐");
        System.out.printf ("│ %-8s │ %-21s │ %-10s │ %-14s │ %-14s │\n",
            headers[0], headers[1], headers[2], headers[3], headers[4]);
        System.out.println("├──────────┼───────────────────────┼────────────┼────────────────┼────────────────┤");
    }

    // Override footer for 5-column transaction layout
    @Override
    protected void printFooter() {
        System.out.println("└──────────┴───────────────────────┴────────────┴────────────────┴────────────────┘");
    }
}
