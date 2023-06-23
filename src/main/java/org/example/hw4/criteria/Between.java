package org.example.hw4.criteria;

import java.util.ArrayList;

public class Between<NumericType> implements Criterion {
    public Between(String columnName, NumericType min, NumericType max) {
        this.columnName = columnName;
        this.min = min;
        this.max = max;

        assert (min != null);
        assert (max != null);
    }

    @Override
    public String getSQLRepresentation() {
        return columnName + "BETWEEN " + min + " AND " + max;
    }

    @Override
    public Iterable<String> getAllUsedColumnNames() {
        var res = new ArrayList<String>();
        res.add(columnName);
        return res;
    }

    private final String columnName;
    private final NumericType min;
    private final NumericType max;
}
