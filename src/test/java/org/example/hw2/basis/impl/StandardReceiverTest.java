package org.example.hw2.basis.impl;

import org.example.exceptions.CreationException;
import org.example.exceptions.HolderException;
import org.example.exceptions.StorageException;
import org.example.factories.hw2.PacketFactory;
import org.example.factories.hw2.FakeReceiverFactory;
import org.example.factories.operations.OperationFactoryInitializer;
import org.example.hw2.basis.Receiver;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.Storage;
import org.example.utilities.Pair;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StandardReceiverTest {
    @Test
//    @Disabled
    @DisplayName("Adding group")
    public void addingGroupTest() {
        final var groupName = "Example";
        assertTrue(storage.getGroup(groupName).isEmpty());
        var messageChooser = new FakeReceiverMessageChooser(new PacketFactory());
        var receiverFactory = new FakeReceiverFactory();
        try(var receiver = receiverFactory.create(storage, messageChooser)) {
            multipleMessage(receiver, messageChooser, new Pair[] {
                    new Pair(Operations.ADD_GOOD_GROUP, new OperationParams(groupName, null, 0, 0.0)),
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(storage.getGroup(groupName).isPresent());
    }

    @Test
//    @Disabled
    @DisplayName("Adding good")
    public void addingGoodTest() {
        final var groupName = "Group1";
        final var goodName = "Butter";
        final var goodPrice = 54;
        assertTrue(storage.getGood(goodName).isEmpty());
        var messageChooser = new FakeReceiverMessageChooser(new PacketFactory());
        var receiverFactory = new FakeReceiverFactory();
        try(var receiver = receiverFactory.create(storage, messageChooser)) {
            multipleMessage(receiver, messageChooser, new Pair[] {
                    new Pair(Operations.ADD_GOOD_NAME_TO_GROUP, new OperationParams(groupName, goodName, 0, goodPrice)),
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(storage.getGroup(groupName).isPresent());
        assertTrue(storage.getGood(goodName).isPresent());
        assertEquals(goodPrice, storage.getGood(goodName).get().getPrice());
    }

    @Test
//    @Disabled
    @DisplayName("Increasing and decreasing good quantity")
    public void addGoodQuantity() {
        final var goodName = "Milk";
        final var initialQuantity = storage.getGood(goodName).get().getQuantity();
        final var quantityIncrease1 = 29;
        final var quantityIncrease2 = 421;
        final var quantityIncrease3 = 10231;
        final var quantityIncrease4 = 23;
        final var quantityDecrease1 = 1003;
        final var quantityDecrease2 = 13;
        final var quantityDecrease3 = 128;
        final var quantityDecrease4 = 300;
        final var resQuantity = initialQuantity + quantityIncrease1 + quantityIncrease2 + quantityIncrease3 + quantityIncrease4 -
                (quantityDecrease1 + quantityDecrease2 + quantityDecrease3 + quantityDecrease4);
        var messageChooser = new FakeReceiverMessageChooser(new PacketFactory());
        var receiverFactory = new FakeReceiverFactory();
        assertTrue(storage.getGood(goodName).isPresent());
        try(var receiver = receiverFactory.create(storage, messageChooser)) {
            multipleMessage(receiver, messageChooser, new Pair[] {
                    new Pair(Operations.ADD_GOOD_QUANTITY, new OperationParams(null, goodName, quantityIncrease1, 0.0)),
                    new Pair(Operations.ADD_GOOD_QUANTITY, new OperationParams(null, goodName, quantityIncrease2, 0.0)),
                    new Pair(Operations.ADD_GOOD_QUANTITY, new OperationParams(null, goodName, quantityIncrease3, 0.0)),
                    new Pair(Operations.ADD_GOOD_QUANTITY, new OperationParams(null, goodName, quantityIncrease4, 0.0)),

                    new Pair(Operations.SUBTRACT_GOOD_QUANTITY, new OperationParams(null, goodName, quantityDecrease1, 0.0)),
                    new Pair(Operations.SUBTRACT_GOOD_QUANTITY, new OperationParams(null, goodName, quantityDecrease2, 0.0)),
                    new Pair(Operations.SUBTRACT_GOOD_QUANTITY, new OperationParams(null, goodName, quantityDecrease3, 0.0)),
                    new Pair(Operations.SUBTRACT_GOOD_QUANTITY, new OperationParams(null, goodName, quantityDecrease4, 0.0)),
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(storage.getGood(goodName).isPresent());
        assertEquals(resQuantity, storage.getGood(goodName).get().getQuantity());
    }

    @Test
//    @Disabled
    @DisplayName("Increasing and decreasing good quantity")
    public void inappropriateDecreasingQuantity() {
        final var goodName = "Milk";
        final var initialQuantity = storage.getGood(goodName).get().getQuantity();
        final var quantityDecrease = initialQuantity + 1;
        var messageChooser = new FakeReceiverMessageChooser(new PacketFactory());
        var receiverFactory = new FakeReceiverFactory();
        assertTrue(storage.getGood(goodName).isPresent());
        try(var receiver = receiverFactory.create(storage, messageChooser)) {
            multipleMessage(receiver, messageChooser, new Pair[] {
                    new Pair(Operations.SUBTRACT_GOOD_QUANTITY, new OperationParams(null, goodName, quantityDecrease, 0.0)),
            });
        } catch (StorageException ignored) {
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(storage.getGood(goodName).isPresent());
        assertEquals(initialQuantity, storage.getGood(goodName).get().getQuantity());
    }

    @Test
//    @Disabled
    @DisplayName("Modifying a good price")
    public void modifyingGoodPrice() {
        final var goodName = "Milk";
        final var initialPrice = storage.getGood(goodName).get().getPrice();
        final var resPrice = initialPrice + 23;
        var messageChooser = new FakeReceiverMessageChooser(new PacketFactory());
        var receiverFactory = new FakeReceiverFactory();
        assertTrue(storage.getGood(goodName).isPresent());
        try(var receiver = receiverFactory.create(storage, messageChooser)) {
            multipleMessage(receiver, messageChooser, new Pair[] {
                    new Pair(Operations.SET_GOOD_PRICE, new OperationParams(null, goodName, 0, resPrice)),
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(storage.getGood(goodName).isPresent());
        assertEquals(resPrice, storage.getGood(goodName).get().getPrice());
    }

    private int[] getRandomInts(int n, int max) {
        Random random = new Random();
        int[] res = new int[n];
        for(int i = 0; i < res.length; ++i) {
            res[i] = random.nextInt(max);
        }
        return res;
    }

    private int[] negate(int[] arr) {
        var res = new int[arr.length];
        for(int i = 0; i < arr.length; ++i) {
            res[i] = -arr[i];
        }
        return res;
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
    public void setUp() throws StorageException {
//        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        storage = new Storage();
        storage.createGroup(new Group("Group1"));
        storage.createGroup(new Group("Group2"));
        storage.addGoodToGroup(new StandardGood("Milk", 10.0), "Group1");
        storage.addGoodToGroup(new StandardGood("Cereal", 20.0), "Group1");
        storage.addGoodToGroup(new StandardGood("Potatoes", 30.0), "Group2");
        storage.addGoodToGroup(new StandardGood("Beetroot", 40.0), "Group2");
    }

//    @AfterEach
//    public void tearDown() {
//        this.threadPool.shutdown();
//    }
//
//    private ExecutorService threadPool;
    private GroupedGoodStorage storage;
}