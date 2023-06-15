package org.example.utilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ThreadUtils {
    public static void shutDownThreadPool(
            ExecutorService threadPool,
            Runnable waitingHandler,
            Consumer<InterruptedException> exceptionHandler,
            long waitingForShutdownMillis) {
        try {
            threadPool.shutdown();
            while(!threadPool.awaitTermination(waitingForShutdownMillis, TimeUnit.MILLISECONDS)) {
                waitingHandler.run();
            }
        } catch (InterruptedException e) {
            exceptionHandler.accept(e);
        }
    }

    public static void shutDownThreadPool(ExecutorService threadPool, Runnable waitingHandler) {
        shutDownThreadPool(threadPool, waitingHandler, e -> Thread.currentThread().interrupt(), 100L);
    }
}
