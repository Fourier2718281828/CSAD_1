package org.example.hw2.operations;

import org.example.exceptions.storage.StorageException;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;

import java.util.Optional;

public class SubtractGoodQuantityOperation implements Operation{
    public SubtractGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
        this.result = null;
        this.params = null;
    }

    @Override
    public void execute(OperationParams params) throws StorageException {
        var goodName = params.getGoodName();
        var prevGood = storage.getGood(goodName);
        var quantity = prevGood.orElseThrow().getQuantity();
        var price = prevGood.orElseThrow().getPrice();
        var newQuantity = quantity - params.getQuantity();
        if(newQuantity < 0)
            throw new StorageException("Inappropriate quantity " + newQuantity);
        System.out.println("Quantity updated: " + newQuantity);
        storage.updateGood(new StandardGood(goodName, newQuantity, price));

    }

    @Override
    public Optional<Integer> getResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<OperationParams> getParamsResult() {
        return Optional.ofNullable(params);
    }

    private final OperationParams params;
    private final Integer result;
    private final GroupedGoodStorage storage;
}
