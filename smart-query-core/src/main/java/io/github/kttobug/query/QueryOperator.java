package io.github.kttobug.query;

public enum QueryOperator {
    // =,
    EQ,

    // !=
    NE,
    // >,
    GT,
    // >=
    GE,
    // <
    LT,
    // <=
    LE,

    LIKE,
    RIGHT_LIKE,
    LEFT_LIKE,

    IN,
    NOT_IN,

    IS_NULL,
    IS_NOT_NULL,

    BETWEEN,
    NOT_BETWEEN,

    ORDER_BY_ASC,
    ORDER_BY_DESC,

    GROUP_BY,
    HAVING,

    OR,
    AND,

    NEST,
    APPLY,

}