package org.example.hw2.operations;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class AddGoodQuantityOperation implements Operation {
    public AddGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) {
        var goodName = params.getGoodName();
        var quantity = params.getQuantity();
        var prevGood = storage.getGood(goodName);
        var prevQuantity = prevGood.orElseThrow().getQuantity();
        var prevPrice = prevGood.orElseThrow().getPrice();
        try {
            storage.updateGood(new StandardGood(goodName, prevQuantity + quantity, prevPrice));
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
