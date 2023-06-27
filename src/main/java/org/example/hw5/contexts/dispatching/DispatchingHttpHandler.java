package org.example.hw5.contexts.dispatching;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.function.Consumer;

public class DispatchingHttpHandler implements HttpHandler {
    public DispatchingHttpHandler(EndpointDispatcher<Consumer<HttpExchange>> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final var requestType = exchange.getRequestMethod();
        final var uri = exchange.getRequestURI().getPath();
        final var processor = dispatcher.dispatch(requestType, uri)
                .orElseThrow(() -> new IOException("Non-existent endpoint: " + requestType + ": " + uri));
        processor.accept(exchange);
    }

    private final EndpointDispatcher<Consumer<HttpExchange>> dispatcher;
}
