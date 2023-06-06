package org.example.packets.encoding.encryption;

import org.example.exceptions.CryptographicException;

public interface Encryptor {
    byte[] encrypt(byte[] bytes) throws CryptographicException;
}
