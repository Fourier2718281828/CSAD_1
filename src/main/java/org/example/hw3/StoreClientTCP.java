package org.example.hw3;

import org.example.exceptions.ClientException;
import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.PacketCodecFactory;
import org.example.factories.hw2.PacketFactory;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.hw2.operations.OperationParams;
import org.example.hw2.operations.Operations;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;
import org.example.utilities.ServerUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreClientTCP implements TCPClient, AutoCloseable {
    public StoreClientTCP(Codec<Packet> codec, DoubleParamFactory<Packet, Byte, Message> packetFactory) {
        this.codec = codec;
        this.packetFactory = packetFactory;
    }

    @Override
    public void connect(InetAddress serverAddress, int serverPort) throws ClientException {
        if(isConnected())
            throw new ClientException("Cannot connect an already connected client.");
        try {
            socket = new Socket(serverAddress, serverPort);
            istream = socket.getInputStream();
            ostream = socket.getOutputStream();
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }
        isConnected = true;
    }

    @Override
    public void disconnect() throws ClientException {
        if(!isConnected())
            throw new ClientException("Cannot disconnect a non-connected client.");
        try {
            socket.close();
            istream.close();
            ostream.close();
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }
        isConnected = false;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void sendMessage(Operations operationType, OperationParams params) throws ClientException {
        if(!isConnected())
            throw new ClientException("Cannot send when not connected to server.");
        try {
            var messageTxt = params.toString();
            var message = new Message(operationType, userId.getAndIncrement(), messageTxt);
            var packet = packetFactory.create((byte) 0x0, message);
            byte[] encrypted = codec.encrypt(packet);
            System.out.println("Length " + encrypted.length + ": " + Arrays.toString(encrypted));
            var bytes = new byte[] {
                    19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 11, 19, 91, 108, 49, 104, -126, 121, 109, -96, 9,
                    -18, -64, 35, 31, -18, -94, 122, -117, -73, 78, 24, 119, -49, -122, 96, -8, 103, -127, 110, 69, -99, -30, -81, 87, -84
            };
            ostream.write(bytes);
            ostream.flush();
        } catch (CreationException | CodecException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message receiveMessage() throws ClientException {
        if(!isConnected())
            throw new ClientException("Cannot receive when not connected to server.");
        try {
            var readBytes = istream.readAllBytes();
            var decoded = codec.decode(readBytes);
            return decoded.message();
        } catch (IOException | CodecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws ClientException {
        if(isConnected) disconnect();
    }

    public static void main(String[] args) {
        try {
            var serverAddress = InetAddress.getLocalHost();
            var codecFactory = new PacketCodecFactory();
            var codec = codecFactory.create();
            var packetFactory = new PacketFactory();
            try (var client = new StoreClientTCP(codec, packetFactory)){
                client.connect(serverAddress, ServerUtils.PORT);
                client.sendMessage(Operations.GET_GOOD_QUANTITY,
                        new OperationParams("", "Milk", 0, 0));
                var received = client.receiveMessage();
                System.out.println(received);
            } catch (ClientException e) {
                throw new RuntimeException(e);
            }
        } catch (CreationException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private Socket socket;
    private InputStream istream;
    private OutputStream ostream;
    private boolean isConnected;
    private final Codec<Packet> codec;
    private final DoubleParamFactory<Packet, Byte, Message> packetFactory;
    private static final AtomicInteger userId = new AtomicInteger(0);
}
