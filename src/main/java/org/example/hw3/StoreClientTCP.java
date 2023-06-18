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

public class StoreClientTCP implements Client {
    public StoreClientTCP(Codec<Packet> codec, DoubleParamFactory<Packet, Byte, Message> packetFactory) {
        this.codec = codec;
        this.packetFactory = packetFactory;
    }

    private void connect(InetAddress serverAddress, int serverPort) throws ClientException {
        assert(!isConnected);
        try {
            socket = new Socket(serverAddress, serverPort);
            istream = socket.getInputStream();
            ostream = socket.getOutputStream();
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }
        isConnected = true;
    }

    private void disconnect() {
        if(!isConnected) return;
        try {
            socket.close();
            istream.close();
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isConnected = false;
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

    @Override
    public Message sendMessage(InetAddress serverAddress, int serverPort,
                               Operations operationType, OperationParams params) throws ClientException {
        try {
            connect(serverAddress, serverPort);
            var messageTxt = params.toString();
            var message = new Message(operationType, userId.getAndIncrement(), messageTxt);
            var packet = packetFactory.create((byte) 0x0, message);
            byte[] encrypted = codec.encrypt(packet);
            ostream.write(encrypted);
            ostream.flush();

            var readBytes = readAllMessage(istream);
            var decoded = codec.decode(readBytes);
            return decoded.message();
        }  catch (CreationException e) {
            throw new RuntimeException(e);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }

    public static void main(String[] args) {
        try {
            var serverAddress = InetAddress.getLocalHost();
            var codecFactory = new PacketCodecFactory();
            var codec = codecFactory.create();
            var packetFactory = new PacketFactory();
            try {
                var client = new StoreClientTCP(codec, packetFactory);
                var received = client.sendMessage(serverAddress, ServerUtils.PORT, Operations.GET_GOOD_QUANTITY,
                        new OperationParams("", "Milk", 0, 0));
                System.out.println(received);
                received = client.sendMessage(serverAddress, ServerUtils.PORT, Operations.GET_GOOD_QUANTITY,
                        new OperationParams("", "Milk", 0, 0));
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
