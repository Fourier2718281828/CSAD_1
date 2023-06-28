package org.example.hw2.operations;

import org.example.exceptions.storage.StorageException;

import java.util.Optional;

public interface Operation {
    void execute(OperationParams params) throws StorageException;
    Optional<Integer> getResult();
    Optional<OperationParams> getParamsResult();
}
