package org.example.packets.data;

public record Message(
        int cType,
        int bUserId,
        byte[] message
) {}
