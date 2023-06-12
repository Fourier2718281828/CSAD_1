package org.example.hw2.operations;

import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class AddGoodNameToGroup implements Operation {
    public AddGoodNameToGroup(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute() {

    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
