package org.example.hw2.operations;

public class OperationParams {
    public OperationParams(String toParse) {
        var split = toParse.split(" ");
        if(split.length != 4)
            throw new RuntimeException("Invalid string to parse");
        groupName = split[0];
        goodName = split[1];
        quantity = Integer.parseInt(split[2]);
        price = Double.parseDouble(split[3]);
    }

    public OperationParams(String groupName, String goodName, int quantity, double price) {
        this.groupName = groupName;
        this.goodName = goodName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity < 0)
            throw new RuntimeException("Trying to set an invalid quantity.");
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price < 0.0)
            throw new RuntimeException("Trying to set an invalid price.");
        this.price = price;
    }

    @Override
    public String toString() {
        return groupName + ' ' + goodName + ' ' + quantity + ' ' + price;
    }

    private String groupName;
    private String goodName;
    private int quantity;
    private double price;
}
