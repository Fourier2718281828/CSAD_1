package org.example.hw5.operations;

import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.StorageException;
import org.example.hw2.goods.StandardGood;
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
        var groupName = params.getGroupName();
        var goodName = params.getGoodName();
        var goodQuantity = params.getQuantity();
        var goodPrice = params.getPrice();
        if(goodQuantity < 0)
            throw new DataConflictException("Conflict while creating a good " + goodName + ": invalid quantity " + goodQuantity);
        if(goodPrice < -0.00001)
            throw new DataConflictException("Conflict while creating a good " + goodName + ": invalid price " + goodPrice);

        try {
            var good = new StandardGood(goodName, goodQuantity, goodPrice);
            storage.addGoodToGroup(good, groupName);
            result = new OperationParams("", goodName, goodQuantity, goodPrice);
        } catch (StorageException e) {
            throw new DataConflictException(e.getMessage());
        }
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
