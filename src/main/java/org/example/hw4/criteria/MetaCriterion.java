package org.example.hw4.criteria;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MetaCriterion implements Criterion {
    public MetaCriterion(List<Criterion> criteria, BooleanOperator operator) {
        this.criteria = criteria;
        this.operator = operator;
    }

    @Override
    public String getSQLRepresentation() {
        return criteria.stream()
                .map(Criterion::getSQLRepresentation)
                .collect(Collectors.joining(' ' + operator.toString() + ' '));
    }

    @Override
    public Iterable<String> getAllUsedColumnNames() {
        return criteria.stream()
                .map(Criterion::getAllUsedColumnNames)
                .flatMap(iterable -> StreamSupport.stream(iterable.spliterator(), false))
                .collect(Collectors.toList());
    }

    private final List<Criterion> criteria;
    private final BooleanOperator operator;

}
