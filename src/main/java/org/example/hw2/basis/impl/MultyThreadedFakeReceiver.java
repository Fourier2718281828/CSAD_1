package org.example.hw2.basis.impl;

import org.example.hw2.basis.Receiver;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.utilities.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultyThreadedFakeReceiver implements Receiver {
    public MultyThreadedFakeReceiver(Receiver receiver) {
        this.receiver = receiver;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void receiveMessage() {
        try {
            threadPool.submit(receiver::receiveMessage);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void receiveMessageTest(Operations type, OperationParams params) {
        try {
            threadPool.submit(() -> receiver.receiveMessageTest(type, params));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        ThreadUtils.shutDownThreadPool(threadPool,
                () -> System.out.println("Waiting for shutting down receiver's thread pool."));
        receiver.close();
    }

    private final Receiver receiver;
    private final ExecutorService threadPool;
}
