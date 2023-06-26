package org.example.hw3;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.exceptions.StorageException;
import org.example.factories.interfaces.TripleParamFactory;
import org.example.factories.operations.OperationFactoryInitializer;
import org.example.hw2.basis.Receiver;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.RAMStorage;
import org.example.hw3.receivers.UDPReceiverFactory;
import org.example.utilities.ServerUtils;
import org.example.utilities.ThreadUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreServerUDP implements Server {
    public StoreServerUDP(int port, GroupedGoodStorage storage,
            TripleParamFactory<Receiver, DatagramSocket, DatagramPacket, GroupedGoodStorage> receiverFactory) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.storage = storage;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.receiverFactory = receiverFactory;
        this.hasStarted = false;
    }
    @Override
    public void start() {
        hasStarted = true;
        while(hasStarted()) {
            try {
                var buffer = new byte[ServerUtils.MAX_PACKET_SIZE];
                var packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                var receiver = receiverFactory.create(socket, packet, storage);
                threadPool.submit(receiver::receiveMessage);
            } catch (IOException | CreationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        stop();
        socket.close();
        ThreadUtils.shutDownThreadPool(threadPool,
                () -> System.out.println("Waiting for TCP-server's thread pool to shut down"));
    }

    @Override
    public void stop() {
        hasStarted = false;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public static void main(String[] args) throws HolderException, StorageException {
        OperationFactoryInitializer.holdAllOperations();
        var storage = new RAMStorage();
        storage.createGroup(new Group("Products"));
        storage.addGoodToGroup(new StandardGood("Milk", 10, 10), "Products");
        try (var server = new StoreServerUDP(ServerUtils.PORT, storage, new UDPReceiverFactory())) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private volatile boolean hasStarted;
    private final DatagramSocket socket;
    private final GroupedGoodStorage storage;
    private final ExecutorService threadPool;
    private final TripleParamFactory<Receiver, DatagramSocket, DatagramPacket, GroupedGoodStorage> receiverFactory;
}
