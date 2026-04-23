package com.syos.report;

import com.syos.util.DatabaseConnection;
import java.sql.*;

/**
 * Template Method Pattern — Abstract base class defining the skeleton algorithm
 * for generating CLI-formatted reports.
 * 
 * The template method generate() calls abstract hook methods in a fixed order:
 * 1. getTitle()    — report title
 * 2. getQuery()    — SQL query to execute
 * 3. getHeaders()  — column headers
 * 4. formatRow()   — format each data row
 * 
 * Subclasses override these hooks to produce different reports.
 */
public abstract class ReportTemplate {

    /**
     * Template Method — defines the algorithm skeleton.
     * Marked final to prevent subclasses from changing the overall flow.
     */
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

    // ===== Abstract hook methods (subclasses must implement) =====

    /** Returns the report title displayed at the top */
    protected abstract String getTitle();

    /** Returns the SQL query to execute for this report */
    protected abstract String getQuery();

    /** Returns column header names */
    protected abstract String[] getHeaders();

    /** Formats a single row of data from the ResultSet */
    protected abstract String formatRow(ResultSet rs) throws SQLException;

    // ===== Concrete methods (default implementations, can be overridden) =====

    /** Prints the table header with borders */
    protected void printHeader() {
        String[] headers = getHeaders();
        
        // Default 4-column layout
        System.out.println("┌────────────┬────────────────────────────────┬─────────────────┬─────────────────┐");
        System.out.printf ("│ %-10s │ %-30s │ %-15s │ %-15s │\n", 
            headers[0], headers[1], headers[2], headers[3]);
        System.out.println("├────────────┼────────────────────────────────┼─────────────────┼─────────────────┤");
    }

    /** Prints the table footer with borders */
    protected void printFooter() {
        System.out.println("└────────────┴────────────────────────────────┴─────────────────┴─────────────────┘");
    }

    /** Prints a message when no data is found */
    protected void printNoDataMessage() {
        System.out.println("│                          No data found.                                       │");
    }
}
