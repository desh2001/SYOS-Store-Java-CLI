package com.syos.decorator;

public class DiscountDecorator extends BillDecorator {
    private double discountPercentage;

    public DiscountDecorator(BillComponent wrappedBill, double discountPercentage) {
        super(wrappedBill);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculateTotal() {
        double baseTotal = wrappedBill.calculateTotal();
        return baseTotal - (baseTotal * discountPercentage / 100.0);
    }

    @Override
    public String getDescription() {
        return wrappedBill.getDescription() +
               String.format(" → Discount (%.1f%%)", discountPercentage);
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getDiscountAmount() {
        return wrappedBill.calculateTotal() * discountPercentage / 100.0;
    }
}


