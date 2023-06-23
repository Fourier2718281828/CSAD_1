package org.example.hw4.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class In implements Criterion {
    public In(String columnName, String... args) {
        this.columnName = columnName;
        this.args = args;
    }

    @Override
    public String getSQLRepresentation() {
        return columnName + " IN (" +
                Arrays.stream(args)
                .map(Objects::toString)
                .collect(Collectors.joining(", "))
                + ")";
    }

    @Override
    public Iterable<String> getAllUsedColumnNames() {
        var res = new ArrayList<String>();
        res.add(columnName);
        return res;
    }

    private final String columnName;
    private final String[] args;
}
