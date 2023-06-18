package org.example.hw3.receivers;

import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Receiver;
import org.example.utilities.ServerUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class StandardReceiver implements Receiver {
    public StandardReceiver(Socket socket, Decryptor decryptor) {
        this.socket = socket;
        this.decryptor = decryptor;
    }

    @Override
    public void receiveMessage() {
        try {
            var message = readAllMessage(socket.getInputStream());
            decryptor.decrypt(message);
        } catch (IOException e) {
            //TODO
            System.err.println("TODO handle an error: ");
            e.printStackTrace();
        } finally {
            try { //TODO !!!!!!!!!
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
        decryptor.close();
    }

    private byte[] readAllMessage(InputStream inputStream) {
        try {
            var res = new byte[ServerUtils.MAX_PACKET_SIZE];
            var actualSize = inputStream.read(res);
            return Arrays.copyOf(res, actualSize);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private final Socket socket;
    private final Decryptor decryptor;
}
