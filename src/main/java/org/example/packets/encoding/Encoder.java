package org.example.packets.encoding;

import java.util.Optional;

public interface Encoder<Encodable> {
    Optional<byte[]> encode(Encodable encodable);
}
