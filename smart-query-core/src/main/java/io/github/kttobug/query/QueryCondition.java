package io.github.kttobug.query;

/**
 * 查询条件类，用于封装单个查询条件的信息。
 * 
 * <p>该类包含查询条件的三个基本要素：字段名、操作符和值。
 * 通过组合这些要素，可以构建各种类型的查询条件。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * QueryCondition condition = new QueryCondition("name", QueryOperator.EQ, "张三");
 * }</pre>
 * 
 * @author kttobug
 * @since 1.0.0
 */
public class QueryCondition {
    
    /** 字段名 */
    private final String field;
    
    /** 操作符 */
    private final QueryOperator operator;
    
    /** 条件值 */
    private final Object value;

    /**
     * 构造函数
     * 
     * @param field 字段名
     * @param operator 操作符
     * @param value 条件值
     */
    public QueryCondition(String field, QueryOperator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * 获取字段名
     * 
     * @return 字段名
     */
    public String getField() {
        return field;
    }

    /**
     * 获取操作符
     * 
     * @return 操作符
     */
    public QueryOperator getOperator() {
        return operator;
    }

    /**
     * 获取条件值
     * 
     * @return 条件值
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "QueryCondition{" +
                "field='" + field + '\'' +
                ", operator=" + operator +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryCondition that = (QueryCondition) o;

        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (operator != that.operator) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
