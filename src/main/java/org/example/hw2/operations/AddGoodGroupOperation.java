package org.example.hw2.operations;

import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class AddGoodGroupOperation implements Operation {
    public AddGoodGroupOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        result = null;
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
