package org.example.hw4.criteria.poset;

import org.example.hw4.criteria.Criterion;

import java.util.ArrayList;

public class POSetOperator<Stringifiable> implements Criterion {
    public POSetOperator(String columnName, String operatorRepresentation, Stringifiable stringifiable) {
        this.columnName = columnName;
        this.operatorRepresentation = operatorRepresentation;
        this.stringifiable = stringifiable;
    }

    @Override
    public String getSQLRepresentation() {
        return columnName + operatorRepresentation + stringifiable.toString();
    }

    @Override
    public Iterable<String> getAllUsedColumnNames() {
        var res = new ArrayList<String>();
        res.add(columnName);
        return res;
    }

    private final String columnName;
    private final String operatorRepresentation;
    private final Stringifiable stringifiable;
}
