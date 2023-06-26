package org.example.hw3;

public interface Server extends AutoCloseable {
    void start();
    void stop();
}
