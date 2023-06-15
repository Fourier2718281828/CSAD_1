package org.example.hw2.goods;

import org.example.exceptions.StorageException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Group implements GoodsGroup {
    public Group(String name) {
        this.name = name;
        this.goods = new ConcurrentHashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterable<Good> getGoods() {
        return Collections.unmodifiableCollection(goods.values());
    }

    @Override
    public void addGood(Good good) {
        goods.put(good.getName(), good);
    }

    @Override
    public void removeGood(String goodName) throws StorageException {
        Good removed = goods.remove(goodName);
        if (removed == null) {
            throw new StorageException("Trying to remove a non-existent good.");
        }
    }

    @Override
    public void updateGood(Good updatedGood) throws StorageException {
        Good gotGood = goods.get(updatedGood.getName());
        if (gotGood == null) {
            throw new StorageException("Non-existent good.");
        }
        goods.put(updatedGood.getName(), updatedGood);
    }

    private final String name;
    private final Map<String, Good> goods;
}
