package org.example.factories;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.hw2.operations.Operation;
import org.example.utilities.ConstructionHolder;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class OperationFactory implements SingleParamFactory<Operation, Integer>,
        ConstructionHolder<Integer, Operation> {
    public OperationFactory() {
        this.creationArchive = new TreeMap<>();
    }

    @Override
    public Operation create(Integer operationId) throws CreationException {
        if(!creationArchive.containsKey(operationId))
            throw new CreationException("Non-existent operation id.");
        var constructor = creationArchive.get(operationId);
        return constructor.get();
    }

    @Override
    public void hold(Integer key, Supplier<Operation> constructor) throws HolderException {
        if(creationArchive.containsKey(key))
            throw new HolderException("Constructor related to the key already held.");
        creationArchive.put(key, constructor);
    }

    private Map<Integer, Supplier<Operation>> creationArchive;
}
