package org.example.utilities;

import org.example.exceptions.HolderException;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class StandardHolder<Key extends Comparable<Key>, Constructable>
        implements Holder<Key, Constructable> {
    public StandardHolder() {
        creationArchive = new TreeMap<>();
    }

    @Override
    public void hold(Key key, Constructable constructor) throws HolderException {
        if(creationArchive.containsKey(key))
            throw new HolderException("Constructor related to the key already held.");
        creationArchive.put(key, constructor);
    }

    @Override
    public void release(Key key) throws HolderException {
        if(!creationArchive.containsKey(key))
            throw new HolderException("Constructor related to the key already held.");
        creationArchive.remove(key);
    }

    @Override
    public Optional<Constructable> getConstructor(Key key) {
        if(!creationArchive.containsKey(key))
            return Optional.empty();
        return Optional.of(creationArchive.get(key));
    }

    private final Map<Key, Constructable> creationArchive;
}
