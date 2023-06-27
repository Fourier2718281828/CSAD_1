package org.example.hw5;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.operations.OperationFactory;
import org.example.hw2.operations.Operations;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw3.Server;
import org.example.hw5.contexts.LoginContext;
import org.example.hw5.contexts.StorageContext;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StandardHttpServerFactory implements DoubleParamFactory<Server, GroupedGoodStorage, InetSocketAddress> {
    @Override
    public Server create(GroupedGoodStorage storage, InetSocketAddress port) throws CreationException {
        try {
            var server = new StandardHttpServer(port, 10);
            var storageContext = new StorageContext(new OperationFactory(storage));
            storageContext.mapEndpointToOperation("GET", "/api/good/{id}", Operations.GET_GOOD);
            storageContext.mapEndpointToOperation("PUT", "/api/good", Operations.CREATE_GOOD);
            storageContext.mapEndpointToOperation("POST", "/api/good{id}", Operations.UPDATE_GOOD);
            storageContext.mapEndpointToOperation("DELETE", "/api/good{id}", Operations.DELETE_GOOD);
            server.addContext("/login", new LoginContext());
            server.addContext("/api", storageContext);
            return server;
        } catch (IOException | HolderException e) {
            throw new CreationException(e.getMessage());
        }
    }
}
