package org.example.hw2.operations;

import java.util.Optional;

public interface Operation {
    void execute(OperationParams params);
    Optional<Integer> getResult();
}
