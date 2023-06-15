package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.GoodsGroup;

import java.util.Optional;

public interface GroupStorage {
    void createGroup(GoodsGroup newGroup) throws StorageException;
    Optional<GoodsGroup> getGroup(String name);
    void updateGroup(GoodsGroup group) throws StorageException;
    void deleteGroup(String name) throws StorageException;
}
