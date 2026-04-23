package com.syos.strategy;

/**
 * Strategy Pattern — Defines the ordering strategy for stock depletion.
 * Different strategies determine which batches are picked first during reshelving.
 */
public interface StockDepletionStrategy {
    /**
     * Returns the SQL ORDER BY clause for selecting stock batches.
     * @return ORDER BY clause string (e.g., "ORDER BY expiry_date ASC")
     */
    String getOrderByClause();

    /**
     * Returns a human-readable name for this strategy.
     * @return Strategy name
     */
    String getStrategyName();
}
