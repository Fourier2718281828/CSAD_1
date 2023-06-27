package org.example.hw5.operations;

import org.example.exceptions.storage.StorageException;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class CreateGoodOperation implements Operation {
    public CreateGoodOperation(GroupedGoodStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute(OperationParams params) throws StorageException {

    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.empty();
    }

    @Override
    public Optional<OperationParams> getParamsResult() {
        return Optional.empty();
    }

    private final GroupedGoodStorage storage;
}
