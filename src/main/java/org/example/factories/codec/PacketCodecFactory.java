package org.example.factories.codec;

import org.example.exceptions.CreationException;
import org.example.factories.interfaces.Factory;
import org.example.packets.data.Packet;
import org.example.packets.encoding.Codec;
import org.example.packets.encoding.PacketCodec;
import org.example.packets.encoding.checksum.CRC16;
import org.example.utilities.bitwise.BigEndianBytePutter;

public class PacketCodecFactory implements Factory<Codec<Packet>> {
    @Override
    public Codec<Packet> create() throws CreationException {
        var bytePutter = new BigEndianBytePutter();
        var messageCodecFactory = new MessageCodecFactory();
        var messageCodec = messageCodecFactory.create();
        var checkSumEvaluator = new CRC16();
        return new PacketCodec(messageCodec, checkSumEvaluator, bytePutter);
    }
}
