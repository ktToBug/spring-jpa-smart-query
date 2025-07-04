package io.github.kttobug.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QueryCondition 单元测试
 * 
 * @author kttobug
 * @since 1.0.0
 */
@DisplayName("QueryCondition 测试")
class QueryConditionTest {

    @Test
    @DisplayName("测试创建查询条件")
    void testCreateQueryCondition() {
        QueryCondition condition = new QueryCondition("name", QueryOperator.EQ, "张三");
        
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.EQ, condition.getOperator());
        assertEquals("张三", condition.getValue());
    }

    @Test
    @DisplayName("测试空值查询条件")
    void testCreateQueryConditionWithNullValue() {
        QueryCondition condition = new QueryCondition("age", QueryOperator.IS_NULL, null);
        
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.IS_NULL, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试空字段名查询条件")
    void testCreateQueryConditionWithEmptyField() {
        QueryCondition condition = new QueryCondition("", QueryOperator.OR, null);
        
        assertEquals("", condition.getField());
        assertEquals(QueryOperator.OR, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试equals方法")
    void testEquals() {
        QueryCondition condition1 = new QueryCondition("name", QueryOperator.EQ, "张三");
        QueryCondition condition2 = new QueryCondition("name", QueryOperator.EQ, "张三");
        QueryCondition condition3 = new QueryCondition("age", QueryOperator.EQ, "张三");
        QueryCondition condition4 = new QueryCondition("name", QueryOperator.NE, "张三");
        QueryCondition condition5 = new QueryCondition("name", QueryOperator.EQ, "李四");
        
        assertEquals(condition1, condition2);
        assertNotEquals(condition1, condition3);
        assertNotEquals(condition1, condition4);
        assertNotEquals(condition1, condition5);
        assertNotEquals(condition1, null);
        assertNotEquals(condition1, "not a QueryCondition");
    }

    @Test
    @DisplayName("测试hashCode方法")
    void testHashCode() {
        QueryCondition condition1 = new QueryCondition("name", QueryOperator.EQ, "张三");
        QueryCondition condition2 = new QueryCondition("name", QueryOperator.EQ, "张三");
        QueryCondition condition3 = new QueryCondition("age", QueryOperator.EQ, "张三");
        
        assertEquals(condition1.hashCode(), condition2.hashCode());
        assertNotEquals(condition1.hashCode(), condition3.hashCode());
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        QueryCondition condition = new QueryCondition("name", QueryOperator.EQ, "张三");
        String result = condition.toString();
        
        assertTrue(result.contains("QueryCondition"));
        assertTrue(result.contains("field='name'"));
        assertTrue(result.contains("operator=EQ"));
        assertTrue(result.contains("value=张三"));
    }

    @Test
    @DisplayName("测试toString方法 - 空值")
    void testToStringWithNullValue() {
        QueryCondition condition = new QueryCondition("age", QueryOperator.IS_NULL, null);
        String result = condition.toString();
        
        assertTrue(result.contains("QueryCondition"));
        assertTrue(result.contains("field='age'"));
        assertTrue(result.contains("operator=IS_NULL"));
        assertTrue(result.contains("value=null"));
    }

    @Test
    @DisplayName("测试不同操作符的查询条件")
    void testDifferentOperators() {
        // 测试所有操作符
        QueryOperator[] operators = QueryOperator.values();
        
        for (QueryOperator operator : operators) {
            QueryCondition condition = new QueryCondition("test", operator, "value");
            assertEquals("test", condition.getField());
            assertEquals(operator, condition.getOperator());
            assertEquals("value", condition.getValue());
        }
    }

    @Test
    @DisplayName("测试复杂值类型")
    void testComplexValueTypes() {
        // 测试数组值
        Object[] arrayValue = {"value1", "value2"};
        QueryCondition condition1 = new QueryCondition("test", QueryOperator.APPLY, arrayValue);
        assertEquals(arrayValue, condition1.getValue());
        
        // 测试集合值
        java.util.List<String> listValue = java.util.Arrays.asList("value1", "value2");
        QueryCondition condition2 = new QueryCondition("test", QueryOperator.IN, listValue);
        assertEquals(listValue, condition2.getValue());
        
        // 测试数字值
        QueryCondition condition3 = new QueryCondition("test", QueryOperator.EQ, 123);
        assertEquals(123, condition3.getValue());
        
        // 测试布尔值
        QueryCondition condition4 = new QueryCondition("test", QueryOperator.EQ, true);
        assertEquals(true, condition4.getValue());
    }
} 