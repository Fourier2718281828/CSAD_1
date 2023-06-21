package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteStorageTest {
    @BeforeEach
    void setUp() {
        storage = SQLiteStorage.getInstance();
    }

    @AfterEach
    void tearDown() {
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
    @DisplayName("Update group ")
    void updateGroupTest() {
        try {
            final var toCreate = new Group("updateGroupTest");
            final var toUpdate = new Group("updatedGroup");
            storage.createGroup(toCreate);
            assertTrue(storage.getGroup(toCreate.getName()).isPresent());
            assertTrue(storage.getGroup(toUpdate.getName()).isEmpty());
            storage.updateGroup(toUpdate);
            assertTrue(storage.getGroup(toUpdate.getName()).isPresent());
            assertTrue(storage.getGroup(toCreate.getName()).isEmpty());
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
    void deleteGoodTest() {
        try {
            final var group =  new Group("deleteGoodTest");
            final var toDelete = new StandardGood("good_deleteGoodTest", 123, 134.23);
            storage.createGroup(group);
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGroup(toDelete.getName()).isEmpty());
            storage.addGoodToGroup(toDelete, group.getName());
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGroup(toDelete.getName()).isPresent());

            storage.deleteGroup(toDelete.getName());
            assertTrue(storage.getGroup(group.getName()).isPresent());
            assertTrue(storage.getGroup(toDelete.getName()).isEmpty());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    private GroupedGoodStorage storage;
}