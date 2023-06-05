package org.example.packets.cryptography;

import org.example.packets.data.Message;

public class MessageCryptographer implements Cryptographer<Message> {
    @Override
    public byte[] encrypt(Message obj) {
        return new byte[0];
    }

    @Override
    public Message decrypt(byte[] bytes) {
        return null;
    }
}
