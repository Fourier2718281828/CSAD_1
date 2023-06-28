package org.example.hw3;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.exceptions.storage.StorageException;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.operations.OperationFactoryInitializer;
import org.example.hw2.basis.Receiver;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.RAMStorage;
import org.example.hw3.receivers.TCPReceiverFactory;
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
        this.hasStarted = false;
    }

    @Override
    public void start() {
        hasStarted = true;
        System.out.println("Server is running");
        while (hasStarted()) {
            Socket clientSocket;
            try {
                clientSocket = socket.accept();
                if(ServerUtils.TCP_SERVER_WILL_BREAK_DOWN) {
                    try {
                        System.out.println("Server broke down");
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Server works again");
                    ServerUtils.TCP_SERVER_WILL_BREAK_DOWN = false;
                } else {
                    var receiver = receiverFactory.create(clientSocket, storage);
                    threadPool.submit(receiver::receiveMessage);
                }
            } catch (IOException | CreationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop() {
        try {
            hasStarted = false;
            socket.close();
            ThreadUtils.shutDownThreadPool(threadPool,
                    () -> System.out.println("Waiting for TCP-server's thread pool to shut down"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public static void main(String[] args) throws HolderException, StorageException, IOException {
        OperationFactoryInitializer.holdAllOperations();
        var storage = new RAMStorage();
        storage.createGroup(new Group("Products"));
        storage.addGoodToGroup(new StandardGood("Milk", 10, 10), "Products");
        var server = new StoreServerTCP(ServerUtils.PORT, new TCPReceiverFactory(), storage);
        server.start();
    }

    private volatile boolean hasStarted;
    private final ServerSocket socket;
    private final DoubleParamFactory<Receiver, Socket, GroupedGoodStorage> receiverFactory;
    private final ExecutorService threadPool;
    private final GroupedGoodStorage storage;
}
