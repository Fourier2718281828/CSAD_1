package org.example.packets.encoding;

public interface Encoder<Encodable> {
    byte[] encode(Encodable encodable);
}
