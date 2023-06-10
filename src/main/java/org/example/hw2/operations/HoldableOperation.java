package org.example.hw2.operations;

import org.example.utilities.ConstructionHolder;

public abstract class HoldableOperation implements Operation {

    protected abstract void getHeld(ConstructionHolder<Integer, HoldableOperation> holder);
}
