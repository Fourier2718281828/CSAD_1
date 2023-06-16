package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;

public class Storage implements GroupedGoodStorage {
    public Storage() {
        this.groups = new ConcurrentHashMap<>();
    }

    @Override
    public void addGoodToGroup(Good good, String groupName) throws StorageException {
        var group = groups.get(groupName);
        if(group == null) {
            throw new StorageException("Trying to add good " + good.getName() + " to a non-existent group: " + groupName);
        }
        group.addGood(good);
    }

    @Override
    public Optional<Good> getGood(String goodName) {
        return groups.values()
                .stream()
                .map(GoodsGroup::getGoods)
                .flatMap(iterable -> StreamSupport.stream(iterable.spliterator(), false))
                .filter(good -> good.getName().equals(goodName))
                .findFirst();
    }

    @Override
    public void updateGood(Good good) throws StorageException {
        var toUpdate = getGood(good.getName())
                .orElseThrow(() -> new StorageException("Trying to get a non-existent good: " + good.getName()));
        toUpdate.setPrice(good.getPrice());
        toUpdate.setQuantity(good.getQuantity());
    }

    @Override
    public void deleteGood(String name) throws StorageException {

    }

    @Override
    public void createGroup(GoodsGroup newGroup) throws StorageException {
        var gotGroup = getGroup(newGroup.getName());
        if(gotGroup.isPresent())
            throw new StorageException("Trying to create an already existent group: " + newGroup);
        groups.put(newGroup.getName(), newGroup);
    }

    @Override
    public Optional<GoodsGroup> getGroup(String name) {
        return Optional.ofNullable(groups.get(name));
    }

    @Override
    public void updateGroup(GoodsGroup group) throws StorageException {

    }

    @Override
    public void deleteGroup(String name) throws StorageException {

    }

    private final Map<String, GoodsGroup> groups;
}
