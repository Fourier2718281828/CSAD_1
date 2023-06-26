package org.example;

import org.example.exceptions.CreationException;
import org.example.exceptions.StorageException;
import org.example.hw2.goods.Good;
import org.example.hw2.goods.Group;
import org.example.hw2.goods.StandardGood;
import org.example.hw2.storages.GroupedGoodStorage;
import org.example.hw2.storages.RAMStorage;
import org.example.hw5.StandardHttpServerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        GroupedGoodStorage storage;
        try {
            storage = new RAMStorage();
            storage.createGroup(new Group("Products"));
            var goods = new ArrayList<Good>();
            goods.add(new StandardGood("Good1", 1, 1.1));
            goods.add(new StandardGood("Good2", 2, 2.2));
            goods.add(new StandardGood("Good3", 3, 3.3));
            for(var good : goods) {
                storage.addGoodToGroup(good, "Products");
            }
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }

        try {
            var serverFactory = new StandardHttpServerFactory();
            var server = serverFactory.create(storage, new InetSocketAddress(8000));
            server.start();
        } catch (CreationException e) {
            throw new RuntimeException(e);
        }
    }
}