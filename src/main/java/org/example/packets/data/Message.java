package org.example.packets.data;

import org.example.hw2.operations.Operations;

public record Message(
        Operations type,
        int userId,
        String message
) {}
