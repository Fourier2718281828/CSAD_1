package org.example.hw5.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.exceptions.HolderException;
import org.example.hw5.DispatchingHttpHandler;
import org.example.hw5.EndpointDispatcher;

import java.io.IOException;

public class StorageContext implements HttpHandler {
    public StorageContext() {
        this.handler = new DispatchingHttpHandler(getBoundDispatcher());
    }

    private EndpointDispatcher getBoundDispatcher() {
        try {
            var dispatcher = new EndpointDispatcher();
            dispatcher.addEndpoint("GET",     "/api/good/{id}", this::getGood);
            dispatcher.addEndpoint("PUT",     "/api/good/{id}", this::createGood);
            dispatcher.addEndpoint("POST",    "/api/good/{id}", this::updateGood);
            dispatcher.addEndpoint("DELETE",  "/api/good/{id}", this::deleteGood);
            return dispatcher;
        } catch (HolderException e) {
            throw new RuntimeException(e);
        }
    }

    private void getGood(HttpExchange exchange) {
        System.out.println(exchange.getRequestMethod() + '/' + exchange.getRequestURI().getPath() + ": getGood");
    }

    private void createGood(HttpExchange exchange) {
        System.out.println(exchange.getRequestMethod() + '/' + exchange.getRequestURI().getPath() + ": createGood");
    }

    private void updateGood(HttpExchange exchange) {
        System.out.println(exchange.getRequestMethod() + '/' + exchange.getRequestURI().getPath() + ": updateGood");
    }

    private void deleteGood(HttpExchange exchange) {
        System.out.println(exchange.getRequestMethod() + '/' + exchange.getRequestURI().getPath() + ": deleteGood");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handler.handle(exchange);
    }

    private final HttpHandler handler;
}
