package org.example.hw4.criteria;

public interface Criterion {
    String getSQLRepresentation();
    Iterable<String> getAllUsedColumnNames();
}
