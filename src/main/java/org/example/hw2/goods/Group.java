package org.example.hw2.goods;

import java.util.LinkedList;
import java.util.List;

public class Group implements GoodsGroup {
    public Group(String name) {
        this.name = name;
        this.goods = new LinkedList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterable<Good> getGoods() {
        return goods;
    }

    @Override
    public void addGood(Good good) {
        goods.add(good);
    }

    private String name;
    private final List<Good> goods;
}
