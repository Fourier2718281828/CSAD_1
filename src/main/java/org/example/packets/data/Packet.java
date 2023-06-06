package org.example.packets.data;

public record Packet(
        byte source,
        long packetId,
        Message message
) {}
