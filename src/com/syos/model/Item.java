package com.syos.model;

public class Item {
    private int id;
    private String code;
    private String name;
    private double price;

    public Item(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public Item(int id, String code, String name, double price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}


