package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteStorageTest {
    @BeforeAll
    public static void setUp() {
        storage = SQLiteStorage.getInstance();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        storage.close();
        var pathToDBFile = "GroupedGoodStorage";
        try {
            Files.delete(Paths.get(pathToDBFile));
        } catch (IOException e) {
            System.err.println("Cannot delete db file.");
        }
    }

    @Test
    @DisplayName("Create and Get group")
    void createAndGetGroupTest() {
        try {
            final var group = new Group("createAndGetGroupTest");
            assertTrue(storage.getGroup(group.getName()).isEmpty());
            storage.createGroup(group);
            assertTrue(storage.getGroup(group.getName()).isPresent());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Disabled
    @DisplayName("Update group ")
    void updateGroupTest() {
        try {
            final var toCreate = new Group("updateGroupTest");
            final var toUpdate = new Group(toCreate.getName());
            final var updatedGood = new StandardGood("UpdatedMilk", 11, 2.9);
            toCreate.addGood(new StandardGood("OldMilk", 132, 3.2));
            toUpdate.addGood(updatedGood);
            storage.createGroup(toCreate);
            assertEquals(toCreate.getName(), toUpdate.getName());
            assertTrue(storage.getGroup(toCreate.getName()).isPresent());

            storage.updateGroup(toUpdate);
            assertTrue(storage.getGroup(toUpdate.getName()).isPresent());
            final var newGroup = storage.getGroup(toUpdate.getName()).get();
            final var iterator = newGroup.getGoods().iterator();
            assertTrue(iterator.hasNext());
            final var good = iterator.next();
            assertEquals(good.getName(), updatedGood.getName());
            assertEquals(good.getQuantity(), updatedGood.getQuantity());
            assertEquals(good.getPrice(), updatedGood.getPrice(), 0.001);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteGroupTest() {
        try {
            final var toDelete =  new Group("deleteGroupTest");
            storage.createGroup(toDelete);
            assertTrue(storage.getGroup(toDelete.getName()).isPresent());
            storage.deleteGroup(toDelete.getName());
            assertTrue(storage.getGroup(toDelete.getName()).isEmpty());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Disabled
    @DisplayName("Create and Read good")
    void createAndReadGoodTest() {
        try {
            final var group = new Group("createGoodTest");
            final var good = new StandardGood("good_createGoodTest", 11, 12);
            storage.createGroup(group);
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGood(good.getName()).isEmpty());
            storage.addGoodToGroup(good, group.getName());
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGood(good.getName()).isPresent());
            var readGood = storage.getGood(good.getName()).get();
            assertEquals(readGood.getName(), good.getName());
            assertEquals(readGood.getQuantity(), good.getQuantity());
            assertEquals(readGood.getPrice(), good.getPrice(), 0.001);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Disabled
    @DisplayName("Update good")
    void updateGoodTest() {
        try {
            final var group = new Group("updateGoodTest");
            final var good = new StandardGood("good_updateGoodTest", 13, 14.2);
            final var toUpdate = new StandardGood(good.getName(), 74, 312.4);
            storage.createGroup(group);
            storage.addGoodToGroup(good, group.getName());
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGood(good.getName()).isPresent());
            var readGood = storage.getGood(good.getName()).get();
            assertEquals(readGood.getName(), good.getName());
            assertEquals(readGood.getQuantity(), good.getQuantity());
            assertEquals(readGood.getPrice(), good.getPrice(), 0.001);

            storage.updateGood(toUpdate);
            assertTrue(storage.getGood(toUpdate.getName()).isPresent());
            var updatedReadGood = storage.getGood(toUpdate.getName()).get();
            assertEquals(updatedReadGood.getName(), toUpdate.getName());
            assertEquals(updatedReadGood.getQuantity(), toUpdate.getQuantity());
            assertEquals(updatedReadGood.getPrice(), toUpdate.getPrice(), 0.001);

            assertTrue(storage.getGroup(group.getName()).isPresent());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Disabled
    void deleteGoodTest() {
        try {
            final var group =  new Group("deleteGoodTest");
            final var toDelete = new StandardGood("good_deleteGoodTest", 123, 134.23);
            storage.createGroup(group);
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGroup(toDelete.getName()).isEmpty());
            storage.addGoodToGroup(toDelete, group.getName());
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGood(toDelete.getName()).isPresent());

            storage.deleteGood(toDelete.getName());
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGroup(toDelete.getName()).isEmpty());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    private static AutoCloseableStorage storage;
}