package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;

import java.util.Optional;

public interface GoodStorage {
    void addGoodToGroup(Good good, String groupName);
    Optional<Good> getGood(String goodName);
    void updateGood(Good good) throws StorageException;
    void deleteGood(String name) throws StorageException;
}
