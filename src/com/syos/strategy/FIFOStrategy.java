package com.syos.strategy;

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


