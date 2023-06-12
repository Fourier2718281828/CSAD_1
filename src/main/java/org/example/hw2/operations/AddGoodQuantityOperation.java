package org.example.hw2.operations;

import org.example.hw2.storages.GroupedGoodStorage;

public class AddGoodQuantityOperation implements Operation {
    public AddGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute() {

    }

    private final GroupedGoodStorage storage;
}
