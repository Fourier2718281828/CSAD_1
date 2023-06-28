package org.example.hw5.contexts.dispatching;

import org.example.exceptions.HolderException;
import org.example.utilities.holders.HashHolder;
import org.example.utilities.holders.Holder;

import java.net.URI;
import java.util.Optional;

public class EndpointDispatcher<Dispatchable> {
    public EndpointDispatcher() {
        this.holder = new HashHolder<>();
    }

    public void addEndpoint(String requestType, String uri, Dispatchable dispatchable) throws HolderException {
        final var key = new EndpointPathTemplate(requestType, uri);
        holder.hold(key, dispatchable);
    }

    public Optional<Dispatchable> dispatch(String method, URI uri) {
        final var key = new EndpointPathTemplate(method, uri);
        return holder.getHoldable(key);
    }

    private final Holder<EndpointPathTemplate, Dispatchable> holder;
}
