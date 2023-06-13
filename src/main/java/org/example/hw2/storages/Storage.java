package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class Storage implements GroupedGoodStorage {
    public Storage() {
        groups = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addGoodToGroup(Good good, String groupName) {
        var foundGroup = getGroup(groupName);
        if(foundGroup.isPresent()) {
            var group = foundGroup.get();
            group.addGood(good);
        } else {
            throw new RuntimeException("Cannot add good with name " +
                    good.getName() + " to a non-existent group with name " + groupName);
        }
    }

    @Override
    public Optional<Good> getGood(String goodName) {
        for(var group : groups) {
            var goods = group.getGoods();
            for(var good : goods) {
                if(good.getName().equals(goodName))
                    return Optional.of(good);
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateGood(Good updatedGood) throws StorageException {
        for(var group : groups) {
            var goods = group.getGoods();
            for(var good : goods) {
                if(good.getName().equals(updatedGood.getName())) {
                    group.updateGood(updatedGood);
                    return;
                }
            }
        }
        throw new StorageException("The good " + updatedGood.getName() + " does not exist.");
    }

    @Override
    public void deleteGood(String goodName) throws StorageException {
        for(var group : groups) {
            var goods = group.getGoods();
            for(var good : goods) {
                if(good.getName().equals(goodName)) {
                    group.removeGood(goodName);
                }
            }
        }
    }

    @Override
    public void createGroup(GoodsGroup newGroup) {
        if(!validateGroupNewName(newGroup.getName()))
            throw new RuntimeException("Invalid group name: the group with name " +
                    newGroup.getName() + " already exists");
        groups.add(newGroup);
    }

    @Override
    public Optional<GoodsGroup> getGroup(String name) {
        return findGroup(group -> group.getName().equals(name));
    }

    public Optional<GoodsGroup> findGroup(Predicate<GoodsGroup> predicate) {
        return groups.stream()
                .filter(predicate)
                .findFirst();
    }

    @Override
    public void updateGroup(GoodsGroup group) {
        var index = indexOfGroup(group.getName());
        groups.set(index, group);
    }

    private int indexOfGroup(String name) {
        int index = 0;
        for(var g : groups) {
            if(g.getName().equals(name)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void deleteGroup(String name) {
        var found = getGroup(name);
        if(found.isPresent())
            groups.remove(found.get());
        else
            throw new RuntimeException("The Group with name " + name + " not found.");
    }

    boolean validateGroupNewName(String newName) {
        return getGroup(newName).isEmpty();
    }

    private final List<GoodsGroup> groups;
}
