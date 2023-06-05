package org.example.packets.data;

public record Packet(
        byte bSrc,
        long bPktId,
        byte[] bMessage
) {}
