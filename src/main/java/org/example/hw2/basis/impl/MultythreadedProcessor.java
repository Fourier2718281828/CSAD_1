package org.example.hw2.basis.impl;

import org.example.hw2.basis.Processor;
import org.example.packets.data.Packet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultythreadedProcessor implements Processor {
    public MultythreadedProcessor(Processor processor) {
        this.processor = processor;
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void process(Packet packet) {
        threadPool.submit(() -> processor.process(packet));
    }

    @Override
    public void close() {
        threadPool.shutdown();
    }

    private final Processor processor;
    private final ExecutorService threadPool;
}
