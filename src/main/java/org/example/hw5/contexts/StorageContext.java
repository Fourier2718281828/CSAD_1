package org.example.hw5.contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.exceptions.storage.DataConflictException;
import org.example.exceptions.storage.NotFoundException;
import org.example.exceptions.storage.StorageException;
import org.example.factories.interfaces.SingleParamFactory;
import org.example.hw2.operations.Operation;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.hw5.contexts.dispatching.EndpointDispatcher;
import org.example.utilities.HttpUtils;

import java.io.IOException;

public class StorageContext implements HttpHandler {
    public StorageContext(SingleParamFactory<Operation, Operations> operationFactory) {
        this.operationFactory = operationFactory;
        this.operationDispatcher = new EndpointDispatcher<>();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final var requestMethod = exchange.getRequestMethod();
            final var uri = exchange.getRequestURI();
            final var operationType = operationDispatcher.dispatch(requestMethod, uri)
                    .orElseThrow(() -> new NotFoundException("Non-handled endpoint: " + requestMethod + ": " + uri));

            final var operation = operationFactory.create(operationType);
            OperationParams params = HttpUtils.fromBody(exchange)
                    .or(() -> HttpUtils.extractParamsFromQuery(exchange))
                    .orElse(new OperationParams());
            operation.execute(params);
            System.out.println("Request to " + uri + " with params: " + params);

            final var result = operation.getParamsResult();
            final var toBody = result.orElse(null);
            final var code = result.isEmpty() ? 204
                    : requestMethod.equalsIgnoreCase("put") ? 201
                    : 200;
            HttpUtils.sendResponse(exchange, code, toBody);
        } catch (DataConflictException e) {
            HttpUtils.sendResponse(exchange, 409);
        } catch (NotFoundException e) {
            HttpUtils.sendResponse(exchange, 404);
        } catch (CreationException | StorageException e) {
            throw new RuntimeException(e);
        }
    }

    public void mapEndpointToOperation(String requestMethod, String uri, Operations operationType) throws HolderException {
        operationDispatcher.addEndpoint(requestMethod, uri, operationType);
    }

    private final EndpointDispatcher<Operations> operationDispatcher;
    private final SingleParamFactory<Operation, Operations> operationFactory;
}
