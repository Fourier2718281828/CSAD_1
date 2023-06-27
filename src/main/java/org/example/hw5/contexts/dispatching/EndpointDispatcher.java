package org.example.hw5.contexts.dispatching;

import org.example.exceptions.HolderException;
import org.example.utilities.Holder;
import org.example.utilities.StandardHolder;

import java.util.Optional;

public class EndpointDispatcher<Dispatchable> {
    public EndpointDispatcher() {
        this.holder = new StandardHolder<>();
    }

    public void addEndpoint(String requestType, String uri, Dispatchable dispatchable) throws HolderException {
        final var key = toKey(requestType, uri);
        holder.hold(key, dispatchable);
    }

    public Optional<Dispatchable> dispatch(String requestType, String uri) {
        final var key = toKey(requestType, uri);
        return holder.getHoldable(key);
    }

    private String toKey(String requestType, String uri) {
        return requestType.toLowerCase() + '#' + uri;
    }

    private final Holder<String, Dispatchable> holder;
}
