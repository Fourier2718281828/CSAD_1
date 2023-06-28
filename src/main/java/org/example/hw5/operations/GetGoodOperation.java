package org.example.hw5.operations;

import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class GetGoodOperation implements Operation {
    public GetGoodOperation(GroupedGoodStorage storage) {
        this.storage = storage;
    }
    @Override
    public void execute(OperationParams params) throws StorageException {
        var goodName = params.getGoodName();
        var good = storage.getGood(goodName)
                .orElseThrow(() -> new NotFoundException("The good " + goodName + " isn't found."));
        result = new OperationParams("", good.getName(), good.getQuantity(), good.getPrice());
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.empty();
    }

    @Override
    public Optional<OperationParams> getParamsResult() {
        return Optional.of(result);
    }

    private OperationParams result;
    private final GroupedGoodStorage storage;
}
