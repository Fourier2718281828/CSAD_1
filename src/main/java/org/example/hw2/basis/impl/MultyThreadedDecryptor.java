package org.example.hw2.basis.impl;

import org.example.hw2.basis.Decryptor;
import org.example.utilities.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultyThreadedDecryptor implements Decryptor {
    public MultyThreadedDecryptor(Decryptor decryptor) {
        this.decryptor = decryptor;
        this.threadPool = Executors.newFixedThreadPool(4);//Runtime.getRuntime().availableProcessors());
    }
    @Override
    public void decrypt(byte[] message) {
        try {
            threadPool.submit(() -> decryptor.decrypt(message));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        ThreadUtils.shutDownThreadPool(threadPool,
                () -> System.out.println("Waiting for shutting down decryptor's thread pool."));
        decryptor.close();
    }

    private final Decryptor decryptor;
    private final ExecutorService threadPool;
}
