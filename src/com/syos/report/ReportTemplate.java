package com.syos.report;

import com.syos.util.DatabaseConnection;
import java.sql.*;

public abstract class ReportTemplate {

    public final void generate() throws SQLException {
        System.out.println("\n --- " + getTitle() + " ---");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(getQuery())) {

            printHeader();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println(formatRow(rs));
            }

            if (!hasData) {
                printNoDataMessage();
            }

            printFooter();
        }
    }

    protected abstract String getTitle();

    protected abstract String getQuery();

    protected abstract String[] getHeaders();

    protected abstract String formatRow(ResultSet rs) throws SQLException;

    protected void printHeader() {
        String[] headers = getHeaders();

        System.out.println("┌────────────┬────────────────────────────────┬─────────────────┬─────────────────┐");
        System.out.printf ("│ %-10s │ %-30s │ %-15s │ %-15s │\n",
            headers[0], headers[1], headers[2], headers[3]);
        System.out.println("├────────────┼────────────────────────────────┼─────────────────┼─────────────────┤");
    }

    protected void printFooter() {
        System.out.println("└────────────┴────────────────────────────────┴─────────────────┴─────────────────┘");
    }

    protected void printNoDataMessage() {
        System.out.println("│                          No data found.                                       │");
    }
}


