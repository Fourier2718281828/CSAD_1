package org.example.hw2.basis;

public interface Decryptor extends AutoCloseable{
    void decrypt(byte[] message);
}
