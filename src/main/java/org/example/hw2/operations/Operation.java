package org.example.hw2.operations;

import org.example.exceptions.StorageException;

import java.util.Optional;

public interface Operation {
    void execute(OperationParams params) throws StorageException;
    Optional<Integer> getResult();
}
