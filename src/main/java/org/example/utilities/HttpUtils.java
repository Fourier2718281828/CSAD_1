package org.example.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.example.hw2.operations.OperationParams;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
            return Optional.of(fromJSON(input));
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
        String goodName = extractQueryParam(query, "goodName")
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
        var requestURI = exchange.getRequestURI();
        return requestURI == null ||
                requestURI.getQuery().isEmpty();
    }

    public static Map<String, String> paramsToMap(OperationParams params) {
        return new TreeMap<>() {{
                put("goodName", params.getGoodName());
                put("groupName", params.getGroupName());
                put("quantity", String.valueOf(params.getQuantity()));
                put("price", String.valueOf(params.getPrice()));
            }};
    }

    public static void sendResponse(HttpExchange exchange, int code, OperationParams body) throws JsonProcessingException {
        byte[] bodyBytes;
        if(body != null) {
            var objectMapper = new ObjectMapper();
            var paramsMap = paramsToMap(body);
            bodyBytes = objectMapper.writeValueAsBytes(paramsMap);
        } else {
            bodyBytes = new byte[0];
        }

        try (var responseBody = exchange.getResponseBody()){
            exchange.sendResponseHeaders(code, bodyBytes.length);
            responseBody.write(bodyBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendResponse(HttpExchange exchange, int code) throws JsonProcessingException {
        sendResponse(exchange, code, null);
    }
}
