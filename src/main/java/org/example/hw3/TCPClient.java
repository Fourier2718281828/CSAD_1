package org.example.hw3;

import org.example.exceptions.ClientException;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;

import java.net.InetAddress;

public interface TCPClient {
    Message sendMessage(InetAddress serverAddress, int serverPort,
                        Operations operationType, OperationParams params) throws ClientException;
}
