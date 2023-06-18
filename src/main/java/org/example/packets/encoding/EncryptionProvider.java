package org.example.packets.encoding;

import org.example.exceptions.CodecException;

public interface EncryptionProvider<Encryptable> {
    byte[] encrypt(Encryptable encryptable) throws CodecException;
}
