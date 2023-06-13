package org.example.hw2.basis.impl;

import org.example.hw2.basis.Receiver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultyThreadedFakeReceiver implements Receiver {
    public MultyThreadedFakeReceiver(Receiver receiver) {
        this.receiver = receiver;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void receiveMessage() {
        threadPool.submit(receiver::receiveMessage);
    }

    @Override
    public void close() throws Exception {
        threadPool.shutdown();
        receiver.close();
    }

    private final Receiver receiver;
    private final ExecutorService threadPool;
}
