package org.example.hw2.storages;

import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;

import java.util.Map;
import java.util.Optional;

public class Storage implements GroupedGoodStorage {
    public Storage(Map<Integer, GoodsGroup> groups) {
        this.groups = groups;
    }

    @Override
    public void addGoodToGroup(Good good, String groupName) {

    }

    @Override
    public Optional<Good> getGood(String goodName) {
        return Optional.empty();
    }

    @Override
    public void updateGood(Good good) {

    }

    @Override
    public void deleteGood(String name) {

    }

    @Override
    public void createGroup(GoodsGroup newGroup) {

    }

    @Override
    public Optional<GoodsGroup> getGroup(String name) {
        return Optional.empty();
    }

    @Override
    public void updateGroup(GoodsGroup group) {

    }

    @Override
    public void deleteGroup(String name) {

    }

    private Map<Integer, GoodsGroup> groups;
}
