package org.example.exceptions.storage;

public class DataConflictException extends StorageException {
    public DataConflictException(String message) {
        super(message);
    }
}
