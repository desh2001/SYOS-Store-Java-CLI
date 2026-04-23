package com.syos.decorator;

/**
 * Decorator Pattern — Component interface for bill pricing.
 * Defines the contract for calculating bill totals with optional modifiers
 * (discounts, taxes, etc.) applied dynamically.
 */
public interface BillComponent {
    /**
     * Calculate the total amount for this bill.
     * Decorators wrap this to modify the total.
     * @return The calculated total
     */
    double calculateTotal();

    /**
     * Get a description of the pricing breakdown.
     * Decorators append their modifier descriptions.
     * @return Pricing description string
     */
    String getDescription();
}
