package org.example.hw2.operations;

import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class GetGoodQuantityOperation implements Operation {
    public GetGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) {
        var goodName = params.goodName();
        var good = storage.getGood(goodName);
        result = good.orElseThrow().getQuantity();
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
