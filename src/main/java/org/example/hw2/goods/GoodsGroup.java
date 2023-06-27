package org.example.hw2.goods;

import org.example.exceptions.storage.StorageException;

public interface GoodsGroup {
    String getName();
    Iterable<Good> getGoods();
    void addGood(Good goods);
    void removeGood(String goodName) throws StorageException;
    void updateGood(Good good) throws StorageException;
}
