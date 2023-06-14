package org.example.hw2.basis.impl;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.factories.hw2.PacketFactory;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.factories.interfaces.SingleParamFactory;
import org.example.factories.hw2.FakeReceiverFactory;
import org.example.factories.operations.OperationFactoryInitializer;
import org.example.hw2.basis.Receiver;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.hw2.storages.Storage;
import org.example.packets.data.Message;
import org.example.utilities.Pair;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class StandardReceiverTest {
    @Test
    @DisplayName("Receiving a single message")
    public void singleMessageTest() {
        var messageChooser = new FakeReceiverMessageChooser(new PacketFactory());
        var receiverFactory = new FakeReceiverFactory();
        try(var receiver = receiverFactory.create(storage, messageChooser)) {
            multipleMessage(receiver, messageChooser, new Pair[] {
                    new Pair(Operations.ADD_GOOD_QUANTITY, new OperationParams(null, "Milk", 10, 0)),
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }

        System.out.println(storage.getGood("Milk"));
    }

    private void multipleMessage(Receiver receiver, FakeReceiverMessageChooser messageChooser, Pair<Operations, OperationParams>[] messages) throws CreationException {
        for(var pair : messages) {
            messageChooser.setOperation(pair.first(), pair.second());
            receiver.receiveMessage();
        }
    }

    @BeforeAll
    public static void setUpClass() {
        try {
            OperationFactoryInitializer.holdAllOperations();
        } catch (HolderException e) {
            fail(e.getMessage());
        }
    }

    @AfterAll
    public static void tearAll()  {
        try {
            OperationFactoryInitializer.releaseAllOperations();
        } catch (HolderException e) {
            fail(e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        storage = new Storage();
        storage.createGroup(new Group("Group1"));
        storage.createGroup(new Group("Group2"));
        storage.addGoodToGroup(new StandardGood("Milk", 10.0), "Group1");
        storage.addGoodToGroup(new StandardGood("Cereal", 20.0), "Group1");
        storage.addGoodToGroup(new StandardGood("Potatoes", 30.0), "Group2");
        storage.addGoodToGroup(new StandardGood("Beetroot", 40.0), "Group2");
    }

    @AfterEach
    public void tearDown() {
        this.threadPool.shutdown();
    }

    private ExecutorService threadPool;
    private Storage storage;
}