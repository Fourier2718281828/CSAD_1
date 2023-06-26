package org.example.hw5;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Authenticator;

public interface EndpointContainer {
    void addEndpoint(String url, HttpHandler handler, Authenticator authenticator);
    default void addEndpoint(String url, HttpHandler handler) {
        addEndpoint(url, handler, null);
    }
}
