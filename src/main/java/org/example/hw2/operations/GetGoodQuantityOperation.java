package org.example.hw2.operations;

import org.example.exceptions.StorageException;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class GetGoodQuantityOperation implements Operation {
    public GetGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) throws StorageException {
        var goodName = params.getGoodName();
        var good = storage.getGood(goodName);
        result = good
                .orElseThrow(() -> new StorageException("Good with name " + goodName + " not found."))
                .getQuantity();
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
