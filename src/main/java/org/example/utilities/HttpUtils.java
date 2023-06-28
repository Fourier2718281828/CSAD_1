package org.example.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.example.hw2.operations.OperationParams;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;

public class HttpUtils {
    public static OperationParams fromJSON(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var mapRepresentation = objectMapper.readValue(bytes, new TypeReference<Map<String, String>>() {});

        var groupName = mapRepresentation.get("groupName");
        var goodName = mapRepresentation.get("goodName");
        var quantityString = mapRepresentation.get("quantity");
        var priceString = mapRepresentation.get("price");

        groupName = groupName == null ? "" : groupName;
        goodName = goodName == null ? "" : goodName;
        int quantity;
        double price;
        try {
            quantity = quantityString == null ? 0 : Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            throw new InputMismatchException("Quantity field does not have an integer representation in the body.");
        }
        try {
            price = priceString == null ? 0.0 : Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            throw new InputMismatchException("Price field does not have a double representation in the body.");
        }
        return new OperationParams(groupName, goodName, quantity, price);
    }

    public static Optional<OperationParams> fromBody(HttpExchange exchange) throws IOException {
        if(hasEmptyBody(exchange))
            return Optional.empty();
        try(var bodyStream = exchange.getRequestBody()) {
            byte[] input = new byte[bodyStream.available()];
            var success = bodyStream.read(input);
            if (success == -1)
                throw new IOException("Empty body input.");
            var params = fromJSON(input);
            if(!HttpUtils.hasEmptyQueryParams(exchange))
                params.setGoodName(HttpUtils.extractQueryParam(exchange, "id").orElseThrow());
            return Optional.of(params);
            //return Optional.of(fromJSON(input));
        }
    }

    public static Optional<String> extractQueryParam(String query, String paramName) {
        if (query != null) {
            var tokens = query.split("&");
            for (String token : tokens) {
                var params = token.split("=");
                if (params.length == 2 && params[0].equals(paramName)) {
                    return Optional.of(params[1]);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<String> extractQueryParam(HttpExchange exchange, String paramName) {
        var query = exchange.getRequestURI().getQuery();
        return extractQueryParam(query, paramName);
    }

    public static Optional<OperationParams> extractParamsFromQuery(HttpExchange exchange) {
        if(hasEmptyQueryParams(exchange))
            return Optional.empty();
        return extractParamsFromQuery(exchange.getRequestURI().getQuery());
    }
    public static Optional<OperationParams> extractParamsFromQuery(String query) {
        String groupName = extractQueryParam(query, "groupName")
                .orElse("");
        String goodName = extractQueryParam(query, "id")
                .orElse("");
        int quantity;
        double price;

        try {
            quantity = extractQueryParam(query, "quantity")
                    .map(Integer::parseInt)
                    .orElse(0);
        } catch(NumberFormatException e) {
            throw new InputMismatchException("Quantity query param representation cannot be parsed as Integer.");
        }

        try {
            price = extractQueryParam(query, "price")
                    .map(Double::parseDouble)
                    .orElse(0.0);
        } catch(NumberFormatException e) {
            throw new InputMismatchException("Price query param representation cannot be parsed as Double.");
        }

        return Optional.of(new OperationParams(groupName, goodName, quantity, price));
    }

    public static boolean hasEmptyBody(HttpExchange exchange) {
        var contentLength = exchange.getRequestHeaders().getFirst("Content-Length");
        return contentLength == null ||
                Integer.parseInt(contentLength) == 0;
    }

    public static boolean hasEmptyQueryParams(HttpExchange exchange) {
        var requestQuery = exchange.getRequestURI().getQuery();
        return requestQuery == null || requestQuery.isEmpty();
    }

    public static void sendResponse(HttpExchange exchange, int code, String message) {
        sendResponseObject(exchange, code, message);
    }

    public static void sendResponse(HttpExchange exchange, int code, OperationParams body) {
        sendResponseObject(exchange, code, body);
    }

    public static void sendResponse(HttpExchange exchange, int code) {
        sendResponseObject(exchange, code, null);
    }

    public static void sendResponseObject(HttpExchange exchange, int code, Object obj) {
        try (var responseBody = exchange.getResponseBody()){
            byte[] bytes;
            if(obj != null) {
                var objectMapper = new ObjectMapper();
                bytes = objectMapper.writeValueAsBytes(obj);
            } else {
                bytes = new byte[0];
            }
            exchange.sendResponseHeaders(code, bytes.length == 0 ? -1 : bytes.length);
            if(bytes.length != 0) responseBody.write(bytes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
