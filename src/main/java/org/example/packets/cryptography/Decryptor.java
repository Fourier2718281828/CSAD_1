package org.example.packets.cryptography;

public interface Decryptor<Decryptable> {
    Decryptable decrypt(byte[] bytes);
}
