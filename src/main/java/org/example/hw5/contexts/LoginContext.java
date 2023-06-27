package org.example.hw5.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.exceptions.HolderException;
import org.example.hw5.contexts.dispatching.DispatchingHttpHandler;
import org.example.hw5.contexts.dispatching.EndpointDispatcher;
import org.example.utilities.HttpUtils;

import java.io.IOException;
import java.util.function.Consumer;

public class LoginContext implements HttpHandler {
    public LoginContext() {
        this.handler = new DispatchingHttpHandler(getBoundDispatcher());
    }

    private EndpointDispatcher<Consumer<HttpExchange>> getBoundDispatcher() {
        try {
            var dispatcher = new EndpointDispatcher<Consumer<HttpExchange>>();
            dispatcher.addEndpoint("POST", "/login", this::login);
            return dispatcher;
        } catch (HolderException e) {
            throw new RuntimeException(e);
        }
    }

    private void login(HttpExchange exchange) {
        System.out.println(exchange.getRequestMethod() + '#' + exchange.getRequestURI().getPath() + ": login");
        HttpUtils.sendResponse(exchange, 200);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handler.handle(exchange);
    }

    private final HttpHandler handler;
}
