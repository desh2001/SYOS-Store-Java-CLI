package com.syos.strategy;

public interface StockDepletionStrategy {

    String getOrderByClause();

    String getStrategyName();
}


