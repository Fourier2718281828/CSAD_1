package org.example.utilities.holders;

import org.example.exceptions.HolderException;

import java.util.Map;
import java.util.Optional;

public class MapHolder<Key, Holdable>
        implements Holder<Key, Holdable> {
    public MapHolder(Map<Key, Holdable> keyToHoldable) {
        this.keyToHoldable = keyToHoldable;
    }

    @Override
    public void hold(Key key, Holdable holdable) throws HolderException {
        if(keyToHoldable.containsKey(key))
            throw new HolderException("Holdable related to the key " + key.toString() + " already held.");
        keyToHoldable.put(key, holdable);
    }

    @Override
    public void release(Key key) throws HolderException {
        if(!keyToHoldable.containsKey(key))
            throw new HolderException("Holdable related to the key " + key.toString() + " already held.");
        keyToHoldable.remove(key);
    }

    @Override
    public Optional<Holdable> getHoldable(Key key) {
        if(!keyToHoldable.containsKey(key))
            return Optional.empty();
        return Optional.of(keyToHoldable.get(key));
    }

    @Override
    public boolean holds(Key key) {
        return keyToHoldable.containsKey(key);
    }

    private final Map<Key, Holdable> keyToHoldable;
}
