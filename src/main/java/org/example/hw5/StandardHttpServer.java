package org.example.hw5;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Authenticator;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StandardHttpServer implements ContexedServer {
    public StandardHttpServer(InetSocketAddress port, int backLog) throws IOException {
        this.server = HttpServer.create();
        server.bind(port, backLog);
        server.setExecutor(null);
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void stop() {
        final int delay = 100;
        server.stop(delay);
    }

    @Override
    public void addContext(String url, HttpHandler handler, Authenticator authenticator) {
        System.out.println("Added endpoint: " + url);
        final var context = server.createContext(url, handler);
        context.setAuthenticator(authenticator);
    }

    private final HttpServer server;
}
