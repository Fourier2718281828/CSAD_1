package org.example.packets.encoding;

import static org.junit.jupiter.api.Assertions.*;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.MessageCodecFactory;
import org.example.factories.codec.PacketCodecFactory;
import org.example.packets.data.Message;
import org.example.packets.data.Packet;
import org.example.utilities.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CodecTest {
    @Test
    @DisplayName("Decoding an encoded empty message")
    void decodingEncodedEmptyMessageTest() {
        Message empty = new Message(0, 0, "");
        decodingEncodedMessage(empty);
    }

    @Test
    @DisplayName("Decoding an encoded random message")
    void decodingEncodedRandomMessageTest() {
        Message random1 = new Message(2, 100, "asdadvasdfasdfwefadf");
        Message random2 = new Message(0, 1030, "asasdfasdfasdfasdfdadvasdfasdfwefadf");
        decodingEncodedMessage(random1);
        decodingEncodedMessage(random2);
    }

    void decodingEncodedMessage(Message message) {
        try {
            var messageCodec = new MessageCodecFactory().create();
            var encoded = messageCodec.encode(message);
            var decoded = messageCodec.decode(encoded);
            assertEquals(decoded, message);
        } catch (CreationException | CodecException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void decodeErrorHandling() {
        try {
            var factory = new PacketCodecFactory();
            decodeInvalidPackets(factory.create());
        } catch (CreationException e) {
            fail("Unhandled error: " + e.getMessage());
        }
    }

    void decodeInvalidPackets(Codec<Packet> codec) {
        for(var packetBytes : invalidPackets) {
            try {
                codec.decode(packetBytes.second());
                fail("Not handled: " + packetBytes.first());
            } catch (CodecException ignored) {
            }
        }
    }

//    void assignChecksum(byte[] packet) {
//        final var checksumEvaluator = new CRC16();
//        final var firstChecksum = checksumEvaluator.evaluateChecksumRange(packet, 0, 14);
//        final var secondChecksum = checksumEvaluator.evaluateChecksumRange(packet, 16, packet.length - 2);
//        packet[14] = (byte)(firstChecksum & 0xFF);
//        packet[15] = (byte)((firstChecksum >>> 2) & 0xFF);
//        packet[packet.length - 2] = (byte)(secondChecksum & 0xFF);
//        packet[packet.length - 1] = (byte)((secondChecksum >>> 2) & 0xFF);
//    }

//    @Test
//    void checkLast() throws CreationException {
//        try {
//            var packetBytes = invalidPackets.get(2);
//            var codec = new PacketCodecFactory().create();
//            System.out.println("NEEEEEDED DECODEING:");
//            codec.decode(packetBytes.second());
//            fail("Not handled: " + packetBytes.first());
//        } catch (CodecException ignored) {
//            System.out.println("CODEC EXCEPTION!");
//        }
//    }

    @BeforeAll
    public static void setUpClass() {
        invalidPackets = new ArrayList<>();
        invalidPackets.add(new Pair<>("bMagic is incorrect", new byte[] {
                                0x12,
                                0x00,
                                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                                0x00, 0x00, 0x00, 0x09, //wLen = 9
                                0x00, 0x00,
                                0x00, 0x00, 0x00, 0x00,   0x00, 0x00, 0x00, 0x00,  0x23,
                                0x00, 0x00
                        }));
        invalidPackets.add(new Pair<>("Incorrect length", new byte[] {
                0x13,
                0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x09, //wLen = 9
                0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,   0x00, 0x00, 0x00, 0x00,  0x23,
                0x00
        }));
    }

    private static List<Pair<String, byte[]>> invalidPackets;
}