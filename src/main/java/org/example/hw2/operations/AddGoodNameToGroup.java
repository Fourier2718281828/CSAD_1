package org.example.hw2.operations;

import org.example.hw2.storages.GroupedGoodStorage;

public class AddGoodNameToGroup implements Operation {
    public AddGoodNameToGroup(GroupedGoodStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute() {

    }

    private final GroupedGoodStorage storage;
}
