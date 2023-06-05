package org.example.packets.cryptography;

import org.example.packets.data.Packet;

public class PacketCryptographer implements Cryptographer<Packet> {
    @Override
    public byte[] encrypt(Packet obj) {
        return new byte[0];
    }

    @Override
    public Packet decrypt(byte[] bytes) {
        return null;
    }
}
