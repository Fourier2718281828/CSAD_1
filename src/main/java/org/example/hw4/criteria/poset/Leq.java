package org.example.hw4.criteria.poset;

import org.example.hw4.criteria.Criterion;

public class Leq<NumericType> implements Criterion {
    public Leq(String columnType, NumericType number) {
        this.operator = new POSetOperator<>(columnType, " <= ", number);
    }
    @Override
    public String getSQLRepresentation() {
        return operator.getSQLRepresentation();
    }

    @Override
    public Iterable<String> getAllUsedColumnNames() {
        return operator.getAllUsedColumnNames();
    }

    private final POSetOperator<NumericType> operator;
}
