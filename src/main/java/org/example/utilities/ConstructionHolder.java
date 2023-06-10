package org.example.utilities;

import org.example.exceptions.HolderException;

import java.util.function.Supplier;

public interface ConstructionHolder<Key, Constructable>  {
    void hold(Key key, Supplier<Constructable> constructor) throws HolderException;
}
