package org.example.hw2.operations;

import org.example.exceptions.HolderException;
import org.example.factories.OperationFactory;

public class GetGoodQuantityOperation implements Operation {
    static {
        try {
            OperationFactory
                    .getInstance()
                    .hold(Operations.GET_GOOD_QUANTITY.ordinal(), GetGoodQuantityOperation::new);
        } catch (HolderException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void execute() {

    }
}
