package org.example.hw2.operations;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Group;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class AddGoodGroupOperation implements Operation {
    public AddGoodGroupOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        result = null;
    }

    @Override
    public void execute(OperationParams params) throws StorageException {
        var newGroupName = params.getGroupName();
        var group = new Group(newGroupName);
        storage.createGroup(group);
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
