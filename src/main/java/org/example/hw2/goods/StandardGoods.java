package org.example.hw2.goods;

public class StandardGoods implements Good {
    public StandardGoods(String name) {
        this.name = name;
        this.quantity = 0;
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

    private String name;
    private int quantity;
}
