package io.github.kttobug.query;

public class QueryCondition {
    private final String field;
    private final QueryOperator operator;
    private final Object value;

    public QueryCondition(String field, QueryOperator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public QueryOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}
