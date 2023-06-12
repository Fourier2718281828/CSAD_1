package org.example.hw2.operations;

import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class SubtractGoodQuantityOperation implements Operation{
    public SubtractGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
    }

    @Override
    public void execute(OperationParams params) {
        var goodName = params.goodName();
        var prevGood = storage.getGood(goodName);
        var quantity = prevGood.orElseThrow().getQuantity();
        var price = prevGood.orElseThrow().getPrice();
        var newQuantity = quantity - params.quantity();
        if(newQuantity < 0)
            throw new RuntimeException("Inappropriate quantity.");
        storage.updateGood(new StandardGood(goodName, newQuantity, price));
    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    private Integer result;
    private final GroupedGoodStorage storage;
}
