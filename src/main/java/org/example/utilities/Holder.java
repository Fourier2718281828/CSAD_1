package org.example.utilities;

import org.example.exceptions.HolderException;

import java.util.Optional;

public interface Holder<Key, Holdable>  {
    void hold(Key key, Holdable constructor) throws HolderException;
    void release(Key key) throws HolderException;
    Optional<Holdable> getConstructor(Key key);
}
