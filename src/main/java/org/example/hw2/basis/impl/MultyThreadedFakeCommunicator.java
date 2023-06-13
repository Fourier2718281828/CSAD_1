package org.example.hw2.basis.impl;

import org.example.exceptions.CodecException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultyThreadedFakeCommunicator implements Communicator {
    public MultyThreadedFakeCommunicator(Communicator communicator) {
        this.communicator = communicator;
        this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void receiveMessage() {
        threadPool.submit(communicator::receiveMessage);
    }

    @Override
    public void sendMessage(byte[] message, InetAddress address) throws CodecException {
        communicator.sendMessage(message, address);
    }

    @Override
    public void close() {
        threadPool.shutdown();
    }

    private final Communicator communicator;
    private final ExecutorService threadPool;
}
