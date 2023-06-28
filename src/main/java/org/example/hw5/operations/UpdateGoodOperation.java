package org.example.hw5.operations;

import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class UpdateGoodOperation implements Operation {
    public UpdateGoodOperation(GroupedGoodStorage storage) {
        this.storage = storage;
    }
    @Override
    public void execute(OperationParams params) throws StorageException {
        final var goodName = params.getGoodName();
        final var quantity = params.getQuantity();
        final var price = params.getPrice();
        if(quantity < 0)
            throw new DataConflictException("Conflict while creating a good " + goodName + ": invalid quantity " + quantity);
        if(price < -0.00001)
            throw new DataConflictException("Conflict while creating a good " + goodName + ": invalid price " + price);

        try {
            storage.updateGood(new StandardGood(goodName, quantity, price));
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
