package org.example.hw2.operations;

import org.example.hw2.storages.GroupedGoodStorage;

public class GetGoodQuantityOperation implements Operation {
//    static {
//        try {
//            OperationFactory
//                    .getInstance()
//                    .hold(Operations.GET_GOOD_QUANTITY.ordinal(), GetGoodQuantityOperation::new);
//        } catch (HolderException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public GetGoodQuantityOperation(GroupedGoodStorage storage) {
        this.storage = storage;
    }

    @Override
    public void execute() {

    }

    private final GroupedGoodStorage storage;
}
