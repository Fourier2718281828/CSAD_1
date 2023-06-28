package org.example.hw5.authenticators;

import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpPrincipal;
import org.example.utilities.http.JWTProvider;

import io.jsonwebtoken.security.SignatureException;

public class JWTAuthenticator extends Authenticator {
    @Override
    public Result authenticate(HttpExchange exchange) {
        final var jwtToken = exchange.getRequestHeaders().getFirst("Authorization");
        try {
            if (jwtToken == null)
                return fail();
            final var login = JWTProvider.getLoginFromJwtToken(jwtToken);
            System.out.println("Authenticating: " + login);
            if (!login.equals("admin"))
                return fail();
            return new Success(new HttpPrincipal(login, "Admin"));
        } catch (SignatureException e) {
            return fail();
        }
    }

    private Result fail() {
        return new Failure(403);
    }
}
