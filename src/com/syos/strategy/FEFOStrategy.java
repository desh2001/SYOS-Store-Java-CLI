package com.syos.strategy;

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


