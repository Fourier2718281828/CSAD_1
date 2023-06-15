package org.example.hw2.basis.impl;

import org.example.hw2.basis.Processor;
import org.example.packets.data.Packet;
import org.example.utilities.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultyThreadedProcessor implements Processor {
    public MultyThreadedProcessor(Processor processor) {
        this.processor = processor;
        threadPool = Executors.newFixedThreadPool(4);//Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void process(Packet packet) {
        threadPool.submit(() -> processor.process(packet));
    }

    @Override
    public void close() throws Exception {
        ThreadUtils.shutDownThreadPool(threadPool,
                () -> System.out.println("Waiting for shutting down processor's thread pool."));
        processor.close();
    }

    private final Processor processor;
    private final ExecutorService threadPool;
}
