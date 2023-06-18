package org.example.hw2.basis;


public interface Receiver extends AutoCloseable {
    void receiveMessage();
}
