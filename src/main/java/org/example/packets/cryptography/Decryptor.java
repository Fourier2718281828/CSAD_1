package org.example.packets.cryptography;

import org.example.exceptions.CryptographicalError;

public interface Decryptor<Decryptable> {
    Decryptable decrypt(byte[] bytes) throws CryptographicalError;
}
