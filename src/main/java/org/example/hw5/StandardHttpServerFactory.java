package org.example.hw5;

import org.example.exceptions.CreationException;
import org.example.factories.interfaces.DoubleParamFactory;
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
            server.addEndpoint("/login", new LoginContext());
            server.addEndpoint("/api", new StorageContext());
            return server;
        } catch (IOException e) {
            throw new CreationException(e.getMessage());
        }
    }
}
