package org.example.hw2.goods;

import org.example.exceptions.StorageException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group implements GoodsGroup {
    public Group(String name) {
        this.name = name;
        this.goods = new CopyOnWriteArrayList<>();
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

    @Override
    public void removeGood(String goodName) throws StorageException {
        var foundGood = goods.stream()
                .filter(good -> good.getName().equals(goodName))
                .findFirst()
                .orElseThrow(() -> new StorageException("Non-existent good."));
        goods.remove(foundGood);
    }

    @Override
    public void updateGood(Good updatedGood) throws StorageException {
        var foundGood = goods.stream()
                .filter(good -> good.getName().equals(updatedGood.getName()))
                .findFirst()
                .orElseThrow(() -> new StorageException("Non-existent good."));
        var index = goods.indexOf(foundGood);
        goods.set(index, updatedGood);
    }

    private String name;
    private final List<Good> goods;
}
