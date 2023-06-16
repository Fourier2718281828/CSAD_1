package org.example.hw2.basis;

import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;

public interface Receiver extends AutoCloseable {
    void receiveMessage();
    void receiveMessageTest(Operations type, OperationParams params);
}
