package com.syos.strategy;

/**
 * Strategy Pattern — FIFO (First In, First Out) stock depletion.
 * Picks batches in the order they were added (by stock_id).
 */
public class FIFOStrategy implements StockDepletionStrategy {

    @Override
    public String getOrderByClause() {
        return "ORDER BY stock_id ASC";
    }

    @Override
    public String getStrategyName() {
        return "FIFO (First In, First Out)";
    }
}
