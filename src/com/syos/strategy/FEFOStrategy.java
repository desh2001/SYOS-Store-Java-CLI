package com.syos.strategy;

/**
 * Strategy Pattern — FEFO (First Expired, First Out) stock depletion.
 * Picks batches with the earliest expiry date first.
 * This is the default strategy used by the system.
 */
public class FEFOStrategy implements StockDepletionStrategy {

    @Override
    public String getOrderByClause() {
        return "ORDER BY expiry_date ASC";
    }

    @Override
    public String getStrategyName() {
        return "FEFO (First Expired, First Out)";
    }
}
