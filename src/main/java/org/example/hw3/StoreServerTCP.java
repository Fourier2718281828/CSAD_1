package org.example.hw3;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.exceptions.StorageException;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.operations.OperationFactoryInitializer;
import org.example.hw2.basis.Receiver;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.Storage;
import org.example.hw3.receivers.ReceiverFactory;
import org.example.utilities.ServerUtils;
import org.example.utilities.ThreadUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreServerTCP implements Server {
    public StoreServerTCP(int port, DoubleParamFactory<Receiver, Socket, GroupedGoodStorage> receiverFactory,
                          GroupedGoodStorage storage) throws IOException {
        this.socket = new ServerSocket(port);
        this.receiverFactory = receiverFactory;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.storage = storage;
    }

    @Override
    public void start() {
        System.out.println("Server is running");
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = socket.accept();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                var receiver = receiverFactory.create(clientSocket, storage);
                threadPool.submit(receiver::receiveMessage);
            } catch (IOException | CreationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        ThreadUtils.shutDownThreadPool(threadPool,
                () -> System.out.println("Waiting for TCP-server's thread pool to shut down"));
    }

    public static void main(String[] args) throws HolderException, StorageException {
        OperationFactoryInitializer.holdAllOperations();
        var storage = new Storage();
        storage.createGroup(new Group("Products"));
        storage.addGoodToGroup(new StandardGood("Milk", 10, 10), "Products");
        try (var server = new StoreServerTCP(ServerUtils.PORT, new ReceiverFactory(), storage)) {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ServerSocket socket;
    private final DoubleParamFactory<Receiver, Socket, GroupedGoodStorage> receiverFactory;
    private final ExecutorService threadPool;
    private final GroupedGoodStorage storage;
}
