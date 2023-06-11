package org.example.hw2.goods;

public interface GoodsGroup {
    String getName();
    Iterable<Good> getGoods();
    void addGood(Good goods);
}
