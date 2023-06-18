package org.example.factories.operations;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.factories.interfaces.SingleParamFactory;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.Operations;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.utilities.Holder;
import org.example.utilities.StandardHolder;

import java.util.Optional;
import java.util.function.Function;

public class OperationFactory implements SingleParamFactory<Operation, Operations> {
    static {
        holder = new StandardHolder<>();
    }

    public OperationFactory(GroupedGoodStorage storage) {
        this.storage = storage;
    }

    @Override
    public Operation create(Operations operationId) throws CreationException {
        var constructor = getConstructor(operationId)
                .orElseThrow(() -> new CreationException("Non-existent operation id."));
        return constructor.apply(storage);
    }

    public static void hold(Operations key, Function<GroupedGoodStorage, Operation> constructor) throws HolderException {
        holder.hold(key, constructor);
    }

    public static void release(Operations key) throws HolderException {
        holder.release(key);
    }

    private static Optional<Function<GroupedGoodStorage, Operation>> getConstructor(Operations type) {
        return holder.getHoldable(type);
    }

    public static boolean holds(Operations key) {
        return holder.holds(key);
    }

    private static final Holder<Operations, Function<GroupedGoodStorage, Operation>> holder;
    private final GroupedGoodStorage storage;
}
