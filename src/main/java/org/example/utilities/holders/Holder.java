package org.example.utilities.holders;

import org.example.exceptions.HolderException;

import java.util.Optional;

public interface Holder<Key, Holdable>  {
    void hold(Key key, Holdable constructor) throws HolderException;
    void release(Key key) throws HolderException;
    Optional<Holdable> getHoldable(Key key);
    boolean holds(Key key);
}
