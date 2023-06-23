package org.example.hw4.criteria;

import java.util.ArrayList;

public class Like implements Criterion {
    public Like(String columnName, String expression) {
        this.columnName = columnName;
        this.expression = expression;
    }
    @Override
    public String getSQLRepresentation() {
        return columnName + " LIKE " + '\'' + expression + '\'';
    }

    @Override
    public Iterable<String> getAllUsedColumnNames() {
        var res = new ArrayList<String>();
        res.add(columnName);
        return res;
    }

    private final String columnName;
    private final String expression;
}
