package org.example.hw2.operations;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class SetGoodPriceOperation implements Operation {
    public SetGoodPriceOperation (GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) {
        var goodName = params.getGoodName();
        var newPrice = params.getPrice();
        var prevGood = storage.getGood(goodName);
        var prevQuantity = prevGood.orElseThrow().getQuantity();
        try {
            storage.updateGood(new StandardGood(goodName, prevQuantity, newPrice));
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
