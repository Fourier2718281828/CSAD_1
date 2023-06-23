package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw4.DataBase;
import org.example.hw4.SQLiteStorage;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteStorageTest {
    @BeforeAll
    public static void setUp() {
        storage = SQLiteStorage.getInstance();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        storage.close();
        var pathToDBFile = SQLiteStorage.getFileName();
        try {
            Files.delete(Paths.get(pathToDBFile));
        } catch (IOException e) {
            System.err.println("Cannot delete db file.");
        }
    }

    private static <T> Set<T> toSet(Iterable<T> iterable) {
        var res = new HashSet<T>();
        for(var item : iterable) {
            res.add(item);
        }
        return res;
    }

    private static <T> boolean equalSets(Iterable<T> a, Iterable<T> b) {
        return toSet(a).equals(toSet(b));
    }

    @Test
    @DisplayName("Create and Get group")
    void createAndGetGroupTest() {
        try {
            final var group = new Group("createAndGetGroupTest");
            final var goods = new ArrayList<Good>();
            goods.add(new StandardGood("good1", 12, 31.2));
            goods.add(new StandardGood("good2", 123, 313.98));
            goods.add(new StandardGood("good3", 122, 3121.7));
            goods.forEach(group::addGood);
            assertTrue(storage.getGroup(group.getName()).isEmpty());
            storage.createGroup(group);
            assertTrue(storage.getGroup(group.getName()).isPresent());
            var gotGoods = storage.getGroup(group.getName())
                    .map(GoodsGroup::getGoods);
            assertTrue(gotGoods.isPresent());
            assertTrue(equalSets(goods, gotGoods.get()));
            try {
                storage.createGroup(new Group(group.getName()));
                fail("Db does not throw an exception, when creating an already existent group");
            } catch (StorageException ignored) {

            }
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Update group ")
    void updateGroupTest() {
        try {
            final var toCreate = new Group("updateGroupTest");
            final var toUpdate = new Group(toCreate.getName());
            final var updatedGood = new StandardGood("UpdatedMilk", 11, 2.9);
            toCreate.addGood(new StandardGood("OldMilk", 132, 3.2));
            toCreate.addGood(new StandardGood("OldMilk2", 1323, 3.42));
            toUpdate.addGood(updatedGood);
            storage.createGroup(toCreate);
            assertEquals(toCreate.getName(), toUpdate.getName());
            assertTrue(storage.getGroup(toCreate.getName()).isPresent());
            for(var good : toCreate.getGoods()) {
                assertTrue(storage.getGood(good.getName()).isPresent());
            }
            assertTrue(storage.getGood(updatedGood.getName()).isEmpty());

            storage.updateGroup(toUpdate);
            assertTrue(storage.getGroup(toUpdate.getName()).isPresent());
            final var newGroup = storage.getGroup(toUpdate.getName()).get();
            final var gotGoods = newGroup.getGoods();
            assertTrue(equalSets(gotGoods, toUpdate.getGoods()));
            for(var good : toCreate.getGoods()) {
                assertTrue(storage.getGood(good.getName()).isEmpty());
            }
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete group")
    void deleteGroupTest() {
        try {
            final var toDelete =  new Group("deleteGroupTest");
            toDelete.addGood(new StandardGood("asd1", 12312,312.2));
            toDelete.addGood(new StandardGood("asd2", 1232,312.72));
            toDelete.addGood(new StandardGood("asd3", 1312,32.62));
            storage.createGroup(toDelete);
            assertTrue(storage.getGroup(toDelete.getName()).isPresent());
            for(var good : toDelete.getGoods()) {
                assertTrue(storage.getGood(good.getName()).isPresent());
            }

            storage.deleteGroup(toDelete.getName());
            assertTrue(storage.getGroup(toDelete.getName()).isEmpty());
            for(var good : toDelete.getGoods()) {
                assertTrue(storage.getGood(good.getName()).isEmpty());
            }
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

            try {
                storage.updateGood(new StandardGood("NonExistentGood", 1231, 32.4));
                fail("Db does not throw an exception, when updating a non-existent good");
            } catch (StorageException ignored) {

            }
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete good")
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

            //When good does not exist:
            try {
                storage.deleteGood("NonExistentGood");
                fail("Db does not throw an exception, when deleting a non-existent good");
            } catch (StorageException ignored) {

            }
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    private static DataBase storage;
}