package org.example.hw2.goods;

public class StandardGood implements Good {
    public StandardGood(String name) {
        this(name, 0, 0.0);
    }

    public StandardGood(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        if(!validateQuantity(quantity))
            throw new RuntimeException("Trying to set an invalid goods quantity.");
        this.quantity = quantity;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        if(!validatePrice(price))
            throw new RuntimeException("Trying to set an invalid good's price.");
        this.price = price;
    }

    private boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    private boolean validatePrice(double price) {
        return price >= 0.0;
    }

    private final String name;
    private int quantity;
    private double price;
}
