package org.example.utilities.holders;

import org.example.exceptions.HolderException;

import java.util.HashMap;
import java.util.Optional;

public class HashHolder<Key, Holdable>
        implements Holder<Key, Holdable> {
    public HashHolder() {
        this.holder = new MapHolder<>(new HashMap<>());
    }


    @Override
    public void hold(Key key, Holdable holdable) throws HolderException {
        holder.hold(key, holdable);
    }

    @Override
    public void release(Key key) throws HolderException {
        holder.release(key);
    }

    @Override
    public Optional<Holdable> getHoldable(Key key) {
        return holder.getHoldable(key);
    }

    @Override
    public boolean holds(Key key) {
        return holder.holds(key);
    }

    private final Holder<Key, Holdable> holder;
}
