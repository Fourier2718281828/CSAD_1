package org.example.exceptions.storage;

public class NotFoundException extends StorageException {
    public NotFoundException(String message) {
        super(message);
    }
}
