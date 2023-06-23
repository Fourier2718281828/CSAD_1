package org.example.hw4.criteria;

import org.example.hw4.criteria.poset.POSetOperator;

public class Equals<NumericType> implements Criterion {
    public Equals(String columnType, NumericType number) {
        this.operator = new POSetOperator<>(columnType, " = ", number);
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
