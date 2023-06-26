package org.example.hw5.contexts.dispatching;

import com.sun.net.httpserver.HttpExchange;
import org.example.exceptions.HolderException;
import org.example.utilities.Holder;
import org.example.utilities.StandardHolder;

import java.util.Optional;
import java.util.function.Consumer;

public class EndpointDispatcher {
    public EndpointDispatcher() {
        this.holder = new StandardHolder<>();
    }

    public void addEndpoint(String requestType, String uri, Consumer<HttpExchange> processor) throws HolderException {
        final var key = toKey(requestType, uri);
        holder.hold(key, processor);
    }

    public Optional<Consumer<HttpExchange>> dispatch(String requestType, String uri) {
        final var key = toKey(requestType, uri);
        return holder.getHoldable(key);
    }

    private String toKey(String requestType, String uri) {
        return requestType.toLowerCase() + '#' + uri;
    }

    private final Holder<String, Consumer<HttpExchange>> holder;
}
