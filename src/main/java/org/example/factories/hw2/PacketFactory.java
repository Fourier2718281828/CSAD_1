package org.example.factories.hw2;

import org.example.exceptions.CreationException;
import org.example.factories.interfaces.DoubleParamFactory;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;

import java.util.concurrent.atomic.AtomicLong;

public class PacketFactory implements DoubleParamFactory<Packet, Byte, Message> {
    static {
        packetId = new AtomicLong(0L);
    }

    @Override
    public Packet create(Byte bSrc, Message message) throws CreationException {
        return new Packet(bSrc, packetId.getAndIncrement(), message);
    }

    private static AtomicLong packetId;
}
