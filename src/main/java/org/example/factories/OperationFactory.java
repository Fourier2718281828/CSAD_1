package org.example.factories;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.hw2.operations.Operation;
import org.example.utilities.Holder;
import org.example.utilities.StandardHolder;

import java.util.Optional;
import java.util.function.Supplier;

public class OperationFactory implements SingleParamFactory<Operation, Integer>,
        Holder<Integer, Supplier<Operation>> {
    public OperationFactory() {
        this.holder = new StandardHolder<>();
    }

    @Override
    public Operation create(Integer operationId) throws CreationException {
        var constructor = getConstructor(operationId)
                .orElseThrow(() -> new CreationException("Non-existent operation id."));
        return constructor.get();
    }

    @Override
    public void hold(Integer key, Supplier<Operation> constructor) throws HolderException {
        holder.hold(key, constructor);
    }

    @Override
    public void release(Integer key) throws HolderException {
        holder.release(key);
    }

    @Override
    public Optional<Supplier<Operation>> getConstructor(Integer integer) {
        return holder.getConstructor(integer);
    }

    private final Holder<Integer, Supplier<Operation>> holder;
}
