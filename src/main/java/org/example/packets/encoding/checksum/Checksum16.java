package org.example.packets.encoding.checksum;

public interface Checksum16 {
    short evaluateChecksum(byte[] bytes);
    short evaluateChecksumRange(byte[] bytes, int min, int max);
}
