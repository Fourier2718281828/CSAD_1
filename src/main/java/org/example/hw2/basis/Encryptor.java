package org.example.hw2.basis;

import org.example.packets.data.Packet;

public interface Encryptor {
    byte[] encrypt(Packet packet);
}
