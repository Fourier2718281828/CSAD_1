package org.example.hw2.basis.impl;

import org.example.hw2.basis.Decryptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultythreadedDecryptor implements Decryptor {
    public MultythreadedDecryptor(Decryptor decryptor) {
        this.decryptor = decryptor;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    @Override
    public void decrypt(byte[] message) {
        threadPool.submit(() -> decryptor.decrypt(message));
    }

    @Override
    public void close() {
        threadPool.shutdown();
    }

    private final Decryptor decryptor;
    private final ExecutorService threadPool;
}
