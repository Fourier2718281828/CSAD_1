package org.example.hw2.goods;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StandardGood implements Good {
    public StandardGood(String name, double price) {
        this(name, 0, price);
    }

    public StandardGood(String name, int quantity, double price) {
        this.name = name;
        this.quantity = new AtomicInteger(quantity);
        this.price = new AtomicReference<>(price);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getQuantity() {
        return quantity.get();
    }

    @Override
    public void setQuantity(int quantity) {
        if(!validateQuantity(quantity))
            throw new RuntimeException("Trying to set an invalid goods quantity.");
        this.quantity.set(quantity);
    }

    @Override
    public double getPrice() {
        return price.get();
    }

    @Override
    public void setPrice(double price) {
        if(!validatePrice(price))
            throw new RuntimeException("Trying to set an invalid good's price.");
        this.price.set(price);
    }

    private boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    private boolean validatePrice(double price) {
        return price >= 0.0;
    }

    private final String name;
    private final AtomicInteger quantity;
    private final AtomicReference<Double> price;
}
