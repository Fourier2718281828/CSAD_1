package org.example.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class JSONUtils {
    public static <T> T fromJSON(byte[] bytes, Class<T> essenceClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bytes, essenceClass);
    }

    public static <T> T fromBody(HttpExchange exchange, Class<T> essenceClass) throws IOException {
        try(var bodyStream = exchange.getRequestBody()) {
            byte[] input = new byte[bodyStream.available()];
            var success = bodyStream.read(input);
            if (success == -1)
                throw new IOException("Empty body input.");
            return fromJSON(input, essenceClass);
        }
    }
}
