package org.example.hw2.goods;

public class StandardGood implements Good {
    public StandardGood(String name) {
        this(name, 0);
    }

    public StandardGood(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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

    private boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    private final String name;
    private int quantity;
}
