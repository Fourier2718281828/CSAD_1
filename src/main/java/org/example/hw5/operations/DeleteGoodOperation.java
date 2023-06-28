package org.example.hw5.operations;

import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class DeleteGoodOperation implements Operation {
    public DeleteGoodOperation(GroupedGoodStorage storage) {
        this.storage = storage;
    }
    @Override
    public void execute(OperationParams params) throws StorageException {
        try {
            var goodName = params.getGoodName();
            storage.deleteGood(goodName);
        } catch (StorageException e) {
            throw new NotFoundException(e.getMessage());
        }
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
