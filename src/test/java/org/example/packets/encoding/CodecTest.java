package org.example.packets.encoding;

import static org.junit.jupiter.api.Assertions.*;

import org.example.exceptions.CodecException;
import org.example.exceptions.CreationException;
import org.example.factories.codec.MessageCodecFactory;
import org.example.factories.codec.PacketCodecFactory;
import org.example.hw2.operations.Operations;
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
        Message empty = new Message(Operations.GET_GOOD_QUANTITY, 0, "");
        decodingEncodedMessage(empty);
    }

    @Test
    @DisplayName("Decoding an encoded random message")
    void decodingEncodedRandomMessageTest() {
        Message random1 = new Message(Operations.ADD_GOOD_GROUP, 100, "asdadvasdfasdfwefadf");
        Message random2 = new Message(Operations.SET_GOOD_PRICE, 1030, "asasdfasdfasdfasdfdadvasdfasdfwefadf");
        decodingEncodedMessage(random1);
        decodingEncodedMessage(random2);
    }

    void decodingEncodedMessage(Message message) {
        try {
            var messageCodec = new MessageCodecFactory().create();
            var encoded = messageCodec.encrypt(message);
            var decoded = messageCodec.decode(encoded);
            assertEquals(decoded, message);
        } catch (CreationException | CodecException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Decoding error handling testing")
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


//    @Test
//    @DisplayName("Decoding checksum match testing")
//    void decodeChecksumTest() {
//        try {
//            var cryptographer = new CipherCryptographer();
//            var message = "Capybara";
//            var packet = new Packet((byte)8, 6, new Message(7, 2, message));
//            var encryptedBytes = cryptographer.encrypt(message.getBytes());
//            var messageCodec = new MessageCodec(new BigEndianBytePutter(), cryptographer);
//            var packetCodec = new PacketCodec(messageCodec, new CRC16(), new BigEndianBytePutter());
//            var encoded = packetCodec.encode(packet);
//            System.out.println(encoded.length);
//            var encrSize = encryptedBytes.length;
//            var wrongCrc = new byte[encoded.length];
//            wrongCrc[0] = 0x13;
//            wrongCrc[1] = packet.source();
//
//            wrongCrc[2] = 0x00;
//            wrongCrc[3] = 0x00;
//            wrongCrc[4] = 0x00;
//            wrongCrc[5] = 0x00;
//            wrongCrc[6] = 0x00;
//            wrongCrc[7] = 0x00;
//            wrongCrc[8] = 0x00;
//            wrongCrc[9] = (byte) packet.packetId();
//
//            wrongCrc[10] = 0x00;
//            wrongCrc[11] = 0x00;
//            wrongCrc[12] = 0x00;
//            wrongCrc[13] = 32;
//
//            wrongCrc[14] = encryptedBytes[14];
//            wrongCrc[15] = encryptedBytes[15];
//
//            //bMsq:
//            wrongCrc[16] = 0x00;
//            wrongCrc[17] = 0x00;
//            wrongCrc[18] = 0x00;
//            wrongCrc[19] = (byte) packet.message().type();
//
//            wrongCrc[20] = 0x00;
//            wrongCrc[21] = 0x00;
//            wrongCrc[22] = 0x00;
//            wrongCrc[23] = (byte) packet.message().userId();
//
//            System.arraycopy(encoded, 24, wrongCrc, 24, encrSize);
//
//            wrongCrc[encoded.length - 2] = encoded[encoded.length - 2];
//            wrongCrc[encoded.length - 1] = encoded[encoded.length - 1];
//
//            assertArrayEquals(encoded, wrongCrc);
//        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
//            fail("Unable to construct cryptographer.");
//        } catch (CryptographicException e) {
//            fail("Failed to encrypt a string.");
//        } catch (CodecException e) {
//            fail("Unable to ecode a packet");
//        }
//    }

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