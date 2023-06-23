package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OldRAMStorage implements GroupedGoodStorage {

    public OldRAMStorage() {
        this.groups = new ConcurrentHashMap<>();
    }

    @Override
    public void createGroup(GoodsGroup group) {
        groups.put(group.getName(), group);
    }

    @Override
    public void deleteGroup(String groupName) throws StorageException {
        GoodsGroup removedGroup = groups.remove(groupName);
        if (removedGroup == null) {
            throw new StorageException("Group " + groupName + " does not exist.");
        }
    }

    @Override
    public Optional<GoodsGroup> getGroup(String groupName) {
        return Optional.ofNullable(groups.get(groupName));
    }

    @Override
    public void updateGroup(GoodsGroup group) throws StorageException {
        var gotGroup = groups.get(group.getName());
        if(gotGroup == null)
            throw new StorageException("Attampting to update a non-existent group: " + group.getName());
        groups.put(group.getName(), group);
    }

    @Override
    public void addGoodToGroup(Good good, String groupName) throws StorageException {
        getGroup(groupName)
                .orElseThrow(() -> new StorageException("Trying to modify a non-existent group: " + groupName))
                .addGood(good);
    }

    @Override
    public Optional<Good> getGood(String goodName) {
        for(var group : groups.values()) {
            for(var good : group.getGoods()) {
                if(good.getName().equals(goodName))
                    return Optional.of(good);
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateGood(Good newGood) throws StorageException {
        for(var group : groups.values()) {
            for(var good : group.getGoods()) {
                if(good.getName().equals(newGood.getName())) {
                    group.updateGood(newGood);
                    return;
                }
            }
        }
        throw new StorageException("Trying to update a non-existent good: " + newGood.getName());
    }

    @Override
    public void deleteGood(String name) throws StorageException {
        for(var group : groups.values()) {
            for(var good : group.getGoods()) {
                if(good.getName().equals(name)) {
                    group.removeGood(name);
                    return;
                }
            }
        }
        throw new StorageException("Trying to delete a non-existent good: " + name);
    }

    @Override
    public Iterable<Good> getGoodsListByCriteria() {
        return null;
    }

    private final Map<String, GoodsGroup> groups;
}
