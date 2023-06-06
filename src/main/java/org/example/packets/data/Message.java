package org.example.packets.data;

public record Message(
        int type,
        int userId,
        String message
) {}
