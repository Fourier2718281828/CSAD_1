package org.example.hw2.storages;

import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.GoodsGroup;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw4.DataBase;
import org.example.hw4.SQLiteStorage;
import org.example.hw4.criteria.*;
import org.example.hw4.criteria.poset.Geq;
import org.example.hw4.criteria.poset.Greater;
import org.example.hw4.criteria.poset.Leq;
import org.example.hw4.criteria.poset.Less;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteStorageTest {
    @BeforeAll
    public static void setUp() {
        try {
            storage = SQLiteStorage.getInstance();
            defaultFillDB(storage);
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        storage.close();
        defaultGoods  = null;
        defaultGroups = null;
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

            storage.deleteGroup(group.getName());
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

            storage.deleteGroup(toCreate.getName());
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

            storage.deleteGroup(group.getName());
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

            storage.deleteGroup(group.getName());
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

            storage.deleteGroup(group.getName());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("'LIKE' criteria list")
    public void likelyCriteriaListTest() {
        try {
            var group1Goods = storage.getGoodsListByCriterion(new Like("good_name", "defaultGood1%"));
            var group2Goods = storage.getGoodsListByCriterion(new Like("good_name", "defaultGood2%"));
            var group3Goods = storage.getGoodsListByCriterion(new Like("good_name", "defaultGood3%"));
            var allGoods    = storage.getGoodsListByCriterion(new Like("good_name", "default%"));

            assertTrue(equalSets(group1Goods, defaultGroups.get(0).getGoods()));
            assertTrue(equalSets(group2Goods, defaultGroups.get(1).getGoods()));
            assertTrue(equalSets(group3Goods, defaultGroups.get(2).getGoods()));
            assertTrue(equalSets(allGoods, defaultGoods));

            var group1 = storage.getGroupsListByCriterion(new Like("group_name", "%Group1"));
            var group2 = storage.getGroupsListByCriterion(new Like("group_name", "%Group2"));
            var group3 = storage.getGroupsListByCriterion(new Like("group_name", "%Group3"));
            var allGroups = storage.getGroupsListByCriterion(new Like("group_name", "%Group%"));

            assertTrue(group1.iterator().hasNext());
            assertTrue(group2.iterator().hasNext());
            assertTrue(group3.iterator().hasNext());
            assertTrue(allGroups.iterator().hasNext());

            group1.forEach(group -> {
                assertEquals(group.getName(), defaultGroups.get(0).getName());
                assertTrue(equalSets(group.getGoods(), defaultGroups.get(0).getGoods()));
            });

            group2.forEach(group -> {
                assertEquals(group.getName(), defaultGroups.get(1).getName());
                assertTrue(equalSets(group.getGoods(), defaultGroups.get(1).getGoods()));
            });

            group3.forEach(group -> {
                assertEquals(group.getName(), defaultGroups.get(2).getName());
                assertTrue(equalSets(group.getGoods(), defaultGroups.get(2).getGoods()));
            });

            AtomicInteger groupCount = new AtomicInteger();
            allGroups.forEach(group -> {
                assertTrue(defaultGroups.stream()
                        .anyMatch(defGroup -> defGroup.getName().equals(group.getName()) &&
                                equalSets(defGroup.getGoods(), group.getGoods())));
                groupCount.incrementAndGet();
            });
            assertEquals(groupCount.get(), defaultGroups.size());
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Poset-criteria list")
    public void posetCriteriaListTest() {
        try {
            var group1Goods = storage.getGoodsListByCriterion(new Less<>("price", 3.0));
            var group1Goods2 = storage.getGoodsListByCriterion(new Leq<>("price", 3.0));
            var group3Goods = storage.getGoodsListByCriterion(new Greater<>("price", 5.0));
            var group3Goods2 = storage.getGoodsListByCriterion(new Geq<>("price", 5.0));
            var allGoods    = storage.getGoodsListByCriterion(new Less<>("price", 7.0));
            var allGoods2   = storage.getGoodsListByCriterion(new Greater<>("price", 0.0));
            assertTrue(equalSets(group1Goods, defaultGroups.get(0).getGoods()));
            assertTrue(equalSets(group1Goods2, defaultGroups.get(0).getGoods()));
            assertTrue(equalSets(group3Goods, defaultGroups.get(2).getGoods()));
            assertTrue(equalSets(group3Goods2, defaultGroups.get(2).getGoods()));
            assertTrue(equalSets(allGoods, defaultGoods));
            assertTrue(equalSets(allGoods2, defaultGoods));
        } catch (StorageException e) {
            fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("'BETWEEN' criteria list")
    public void betweenCriteriaListTest() {
        try {
            var group1Goods = storage.getGoodsListByCriterion(new Between<>("quantity", 1, 2));
            var group2Goods = storage.getGoodsListByCriterion(new Between<>("quantity", 3, 4));
            var group3Goods = storage.getGoodsListByCriterion(new Between<>("quantity", 5, 6));
            var allGoods    = storage.getGoodsListByCriterion(new Between<>("quantity", -20, 65));

            assertTrue(equalSets(group1Goods, defaultGroups.get(0).getGoods()));
            assertTrue(equalSets(group2Goods, defaultGroups.get(1).getGoods()));
            assertTrue(equalSets(group3Goods, defaultGroups.get(2).getGoods()));
            assertTrue(equalSets(allGoods, defaultGoods));
        } catch (StorageException e) {
            fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("Meta-criteria list")
    public void metaCriteriaListTest() {
        try {
            var listOfCriteria = new ArrayList<Criterion>();
            listOfCriteria.add(new Greater<>("price", 3.0));
            listOfCriteria.add(new Leq<>("price", 5.0));
            var metaCriterion = new MetaCriterion(listOfCriteria, BooleanOperator.AND);
            var group2Goods = storage.getGoodsListByCriterion(metaCriterion);
            assertTrue(equalSets(group2Goods, defaultGroups.get(1).getGoods()));
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("'IN' criteria test")
    public void inCriteriaTest() {
        try {
            var group2Goods = storage.getGoodsListByCriterion(new In<>("quantity", 3, 4));
            assertTrue(equalSets(group2Goods, defaultGroups.get(1).getGoods()));

            var good21Name = defaultGoods.get(2).getName();
            var good32Name = defaultGoods.get(5).getName();
            var groups23 = storage.getGroupsListByCriterion(new In<>("group_id", """
                    SELECT group_id
                    FROM 'Good'
                    WHERE good_name IN('""" + good21Name + "', '" + good32Name + "')"));
            var names = StreamSupport.stream(groups23.spliterator(), false)
                            .map(GoodsGroup::getName).toList();
            var group23Names =  new ArrayList<String>();
            group23Names.add(defaultGroups.get(1).getName());
            group23Names.add(defaultGroups.get(2).getName());
            assertTrue(equalSets(names, group23Names));
        } catch (StorageException e) {
            fail(e.getMessage());
        }
    }

    private static void defaultFillDB(DataBase db) throws StorageException {
        defaultGoods = new ArrayList<>();
        defaultGoods.add(new StandardGood("defaultGood11", 1, 1.75));
        defaultGoods.add(new StandardGood("defaultGood12", 2, 2.32));
        defaultGoods.add(new StandardGood("defaultGood21", 3, 3.42));
        defaultGoods.add(new StandardGood("defaultGood22", 4, 4.12));
        defaultGoods.add(new StandardGood("defaultGood31", 5, 5.21));
        defaultGoods.add(new StandardGood("defaultGood32", 6, 6.22));

        var group1 = new Group("defaultGroup1");
        group1.addGood(defaultGoods.get(0));
        group1.addGood(defaultGoods.get(1));

        var group2 = new Group("defaultGroup2");
        group2.addGood(defaultGoods.get(2));
        group2.addGood(defaultGoods.get(3));

        var group3 = new Group("defaultGroup3");
        group3.addGood(defaultGoods.get(4));
        group3.addGood(defaultGoods.get(5));

        db.createGroup(group1);
        db.createGroup(group2);
        db.createGroup(group3);

        defaultGroups = new ArrayList<>();
        defaultGroups.add(group1);
        defaultGroups.add(group2);
        defaultGroups.add(group3);
    }

    private static DataBase storage;
    private static ArrayList<Good> defaultGoods;
    private static ArrayList<GoodsGroup> defaultGroups;
}