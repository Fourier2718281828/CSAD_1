package org.example.hw3;

import org.example.exceptions.ClientException;
import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.exceptions.StorageException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.hw2.PacketFactory;
import org.example.factories.operations.OperationFactoryInitializer;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.Storage;
import org.example.hw3.receivers.TCPReceiverFactory;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;
import org.example.utilities.ServerUtils;
import org.example.utilities.ThreadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class TCPClientTest {

    @BeforeEach
    void setUp() {

        try {
            OperationFactoryInitializer.holdAllOperations();
            storage = new Storage();
            storage.createGroup(new Group("Products"));
            storage.addGoodToGroup(new StandardGood("Milk", 11, 12), "Products");
            threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            server = new StoreServerTCP(ServerUtils.PORT, new TCPReceiverFactory(), storage);
            threadPool.submit(server::start);
        } catch (IOException | HolderException | StorageException e) {
            fail(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        threadPool.shutdown();
        server.close();
    }

    @Test
    void severalClientsTest() {
        try {
            final var codecFactory = new PacketCodecFactory();
            final var packetFactory = new PacketFactory();
            final var codec = codecFactory.create();
            Client[] clients = new Client[] {
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
                    new StoreClientTCP(codec, packetFactory),
            };

            final var results = new ConcurrentHashMap<Client, String>();
            final var serverAdress = InetAddress.getLocalHost();
            final var goodName = "Milk";
            final var threadPoolClients = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            for(var client : clients) {
                threadPoolClients.submit(() -> {
                    try {
                        var message = client.sendMessage(serverAdress, ServerUtils.PORT,
                                Operations.GET_GOOD_QUANTITY, new OperationParams("", goodName, 0, 0));
                        results.put(client, message.message());
                    } catch (ClientException e) {
                        fail(e.getMessage());
                    }
                });
            }

            ThreadUtils.shutDownThreadPool(threadPoolClients, () -> System.out.println("Waiting till thread pool has finished."));
            final var expectedQuantityOpt = storage.getGood(goodName).map(Good::getQuantity);
            if(expectedQuantityOpt.isEmpty()) fail("There's no " + goodName + " in storage!");
            final var expectedResult = "Ok. Result = " + expectedQuantityOpt.get();
            for(var result : results.values()) {
                assertEquals(result, expectedResult);
            }
        } catch (CreationException | UnknownHostException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void failedConnectionTest() {
        try {
            final var goodName = "Milk";
            final var codecFactory = new PacketCodecFactory();
            final var packetFactory = new PacketFactory();
            final Codec<Packet> codec = codecFactory.create();
            final var client = new StoreClientTCP(codec, packetFactory);
            ServerUtils.TCP_SERVER_WILL_BREAK_DOWN = true;
            var message = client.sendMessage(ServerUtils.SERVER_IP, ServerUtils.PORT,
                    Operations.GET_GOOD_QUANTITY, new OperationParams("", goodName, 0, 0));
            final var expectedQuantityOpt = storage.getGood(goodName).map(Good::getQuantity);
            if(expectedQuantityOpt.isEmpty()) fail("There's no " + goodName + " in storage!");
            final var expectedResult = "Ok. Result = " + expectedQuantityOpt.get();
            assertEquals(expectedResult, message.message());
            ServerUtils.TCP_SERVER_WILL_BREAK_DOWN = false;
        } catch (CreationException | ClientException e) {
            fail(e);
        }
    }

    private ExecutorService threadPool;
    private Server server;
    private GroupedGoodStorage storage;
}