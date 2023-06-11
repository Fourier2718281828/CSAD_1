package org.example.hw2.storages;

import org.example.hw2.goods.GoodsGroup;

import java.util.Optional;

public interface GroupStorage {
    void createGroup(GoodsGroup newGroup);
    Optional<GoodsGroup> getGroup(String name);
    void updateGroup(GoodsGroup group);
    void deleteGroup(String name);
}
