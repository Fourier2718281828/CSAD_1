package org.example.hw5;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Authenticator;

public interface ContextContainer {
    void addContext(String url, HttpHandler handler, Authenticator authenticator);
    default void addContext(String url, HttpHandler handler) {
        addContext(url, handler, null);
    }
}
