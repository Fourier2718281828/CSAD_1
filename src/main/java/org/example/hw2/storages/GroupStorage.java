package org.example.hw2.storages;

import org.example.exceptions.storage.StorageException;
import org.example.hw2.goods.GoodsGroup;
import org.example.hw4.criteria.Criterion;

import java.util.Optional;

public interface GroupStorage {
    void createGroup(GoodsGroup newGroup) throws StorageException;
    Optional<GoodsGroup> getGroup(String name);
    void updateGroup(GoodsGroup group) throws StorageException;
    void deleteGroup(String name) throws StorageException;
    Iterable<GoodsGroup> getGroupsListByCriterion(Criterion criterion) throws StorageException;
}
