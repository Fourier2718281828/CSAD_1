package org.example.hw5.contexts;

import java.security.MessageDigest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.exceptions.HolderException;
import org.example.hw5.contexts.dispatching.DispatchingHttpHandler;
import org.example.hw5.contexts.dispatching.EndpointDispatcher;
import org.example.utilities.http.HttpUtils;
import org.example.utilities.http.JWTProvider;
import org.example.utilities.http.LoginResponse;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
        var credentials = HttpUtils.credentialsFromBody(exchange);
        var isAuthorized = credentials.isPresent() &&
                credentials.get().login().equals(DEFAULT_LOGIN) &&
                credentials.get().password().equals(DEFAULT_PASSWORD);
        if(isAuthorized)
            sendAuthorisedResponse(exchange, credentials.get().login());
        else
            sendUnauthorisedResponse(exchange);
        return;
    }

    private void sendAuthorisedResponse(HttpExchange exchange, String login) {
        var token = JWTProvider.generateToken(login);
        HttpUtils.sendResponseObject(exchange, 200, new LoginResponse(token));
    }

    private void sendUnauthorisedResponse(HttpExchange exchange) {
        HttpUtils.sendResponse(exchange, 401);
    }

    private String hashPassword(String password) {
        try {
            var md5 = MessageDigest.getInstance("MD5");
            var hash = md5.digest(password.getBytes());
            StringBuilder toHex = new StringBuilder();
            for (byte b : hash) {
                String hexFormat = String.format("%02x", b);
                toHex.append(hexFormat);
            }
            return toHex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handler.handle(exchange);
    }

    private final HttpHandler handler;
    private final String DEFAULT_LOGIN = "admin";
    private final String DEFAULT_PASSWORD = hashPassword("admin");
}
