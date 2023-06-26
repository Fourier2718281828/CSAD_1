package org.example.hw5.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.exceptions.HolderException;
import org.example.hw5.contexts.dispatching.DispatchingHttpHandler;
import org.example.hw5.contexts.dispatching.EndpointDispatcher;

import java.io.IOException;

public class LoginContext implements HttpHandler {
    public LoginContext() {
        this.handler = new DispatchingHttpHandler(getBoundDispatcher());
    }

    private EndpointDispatcher getBoundDispatcher() {
        try {
            var dispatcher = new EndpointDispatcher();
            dispatcher.addEndpoint("POST", "/login", this::login);
            return dispatcher;
        } catch (HolderException e) {
            throw new RuntimeException(e);
        }
    }

    private void login(HttpExchange exchange) {
        System.out.println(exchange.getRequestMethod() + '/' + exchange.getRequestURI().getPath() + ": login");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handler.handle(exchange);
    }

    private final HttpHandler handler;
}
