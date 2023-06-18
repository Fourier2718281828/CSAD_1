package org.example.hw3.receivers;

import org.example.factories.hw3.IStreamProcessorFactory;
import org.example.hw2.basis.Decryptor;
import org.example.hw2.basis.Receiver;
import org.example.utilities.ServerUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class StandardReceiver implements Receiver {
    public StandardReceiver(Socket socket, IStreamProcessorFactory readerFactory, Decryptor decryptor) {
        this.socket = socket;
        this.readerFactory = readerFactory;
        this.decryptor = decryptor;
    }

    @Override
    public void receiveMessage() {
        try /*(var inReader = new BufferedReader(new InputStreamReader(socket.getInputStream())))*/ {
            var message = readAllMessage(socket.getInputStream());
            System.out.println("Received length " + message.length +": " + Arrays.toString(message));
            decryptor.decrypt(message);
        } catch (IOException e) {
            //TODO
            System.err.println("TODO handle an error: ");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try { //TODO !!!!!!!!!
                close();
            } catch (Exception e) {
                throw new RuntimeException(e);
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
    private final IStreamProcessorFactory readerFactory;
    private final Decryptor decryptor;
}
