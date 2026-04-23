package com.syos.model;

public class Item {
    private int id;
    private String code;
    private String name;
    private double price;

    // Constructor without ID (for creating new items)
    public Item(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    // Constructor with ID (for reading from database)
    public Item(int id, String code, String name, double price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}