package io.github.kttobug.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LambdaQueryWrapper 单元测试
 * 
 * @author kttobug
 * @since 1.0.0
 */
@DisplayName("LambdaQueryWrapper 测试")
class LambdaQueryWrapperTest {

    private LambdaQueryWrapper<TestUser> wrapper;

    @BeforeEach
    void setUp() {
        wrapper = LambdaQueryWrapper.of(TestUser.class);
    }

    @Test
    @DisplayName("测试创建查询包装器")
    void testCreateWrapper() {
        assertNotNull(wrapper);
        assertEquals(TestUser.class, wrapper.getEntityClass());
        assertTrue(wrapper.isEmpty());
        assertEquals(0, wrapper.size());
    }

    @Test
    @DisplayName("测试等于条件")
    void testEq() {
        wrapper.eq(TestUser::getName, "张三");
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.EQ, condition.getOperator());
        assertEquals("张三", condition.getValue());
    }

    @Test
    @DisplayName("测试等于条件 - 空值忽略")
    void testEqWithNullValue() {
        wrapper.eq(TestUser::getName, null);
        
        assertEquals(0, wrapper.size());
        assertTrue(wrapper.isEmpty());
    }

    @Test
    @DisplayName("测试不等于条件")
    void testNe() {
        wrapper.ne(TestUser::getAge, 25);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.NE, condition.getOperator());
        assertEquals(25, condition.getValue());
    }

    @Test
    @DisplayName("测试大于条件")
    void testGt() {
        wrapper.gt(TestUser::getAge, 18);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.GT, condition.getOperator());
        assertEquals(18, condition.getValue());
    }

    @Test
    @DisplayName("测试大于等于条件")
    void testGe() {
        List<Integer> values = Arrays.asList(18);
        wrapper.ge(TestUser::getAge, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.GE, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试小于条件")
    void testLt() {
        wrapper.lt(TestUser::getAge, 65);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.LT, condition.getOperator());
        assertEquals(65, condition.getValue());
    }

    @Test
    @DisplayName("测试小于等于条件")
    void testLe() {
        List<Integer> values = Arrays.asList(65);
        wrapper.le(TestUser::getAge, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.LE, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试模糊查询")
    void testLike() {
        wrapper.like(TestUser::getName, "张");
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.LIKE, condition.getOperator());
        assertEquals("张", condition.getValue());
    }

    @Test
    @DisplayName("测试左模糊查询")
    void testLeftLike() {
        List<String> values = Arrays.asList("三");
        wrapper.leftLike(TestUser::getName, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.LEFT_LIKE, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试右模糊查询")
    void testRightLike() {
        List<String> values = Arrays.asList("张");
        wrapper.rightLike(TestUser::getName, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.RIGHT_LIKE, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试IN查询")
    void testIn() {
        List<String> values = Arrays.asList("张三", "李四", "王五");
        wrapper.in(TestUser::getName, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.IN, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试NOT IN查询")
    void testNotIn() {
        List<String> values = Arrays.asList("张三", "李四");
        wrapper.notIn(TestUser::getName, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.NOT_IN, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试IS NULL条件")
    void testIsNull() {
        wrapper.isNull(TestUser::getPhone, Collections.emptyList());
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("phone", condition.getField());
        assertEquals(QueryOperator.IS_NULL, condition.getOperator());
        assertEquals(Collections.emptyList(), condition.getValue());
    }

    @Test
    @DisplayName("测试IS NOT NULL条件")
    void testIsNotNull() {
        wrapper.isNotNull(TestUser::getEmail, Collections.emptyList());
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("email", condition.getField());
        assertEquals(QueryOperator.IS_NOT_NULL, condition.getOperator());
        assertEquals(Collections.emptyList(), condition.getValue());
    }

    @Test
    @DisplayName("测试BETWEEN条件")
    void testBetween() {
        List<Integer> values = Arrays.asList(18, 65);
        wrapper.between(TestUser::getAge, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.BETWEEN, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试NOT BETWEEN条件")
    void testNotBetween() {
        List<Integer> values = Arrays.asList(18, 65);
        wrapper.notBetween(TestUser::getAge, values);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("age", condition.getField());
        assertEquals(QueryOperator.NOT_BETWEEN, condition.getOperator());
        assertEquals(values, condition.getValue());
    }

    @Test
    @DisplayName("测试升序排序")
    void testOrderByAsc() {
        wrapper.orderByAsc(TestUser::getName);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("name", condition.getField());
        assertEquals(QueryOperator.ORDER_BY_ASC, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试降序排序")
    void testOrderByDesc() {
        wrapper.orderByDesc(TestUser::getCreateTime);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("createTime", condition.getField());
        assertEquals(QueryOperator.ORDER_BY_DESC, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试分组")
    void testGroupBy() {
        wrapper.groupBy(TestUser::getDepartment);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("department", condition.getField());
        assertEquals(QueryOperator.GROUP_BY, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试多字段分组")
    void testGroupByMultiple() {
        wrapper.groupBy(TestUser::getDepartment, TestUser::getRole);
        
        assertEquals(2, wrapper.size());
        QueryCondition condition1 = wrapper.getConditions().get(0);
        QueryCondition condition2 = wrapper.getConditions().get(1);
        
        assertEquals("department", condition1.getField());
        assertEquals(QueryOperator.GROUP_BY, condition1.getOperator());
        assertEquals("role", condition2.getField());
        assertEquals(QueryOperator.GROUP_BY, condition2.getOperator());
    }

    @Test
    @DisplayName("测试HAVING条件")
    void testHaving() {
        wrapper.having(TestUser::getSalary, QueryOperator.GT, 5000);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("salary", condition.getField());
        assertEquals(QueryOperator.HAVING, condition.getOperator());
        
        Object[] havingData = (Object[]) condition.getValue();
        assertEquals(QueryOperator.GT, havingData[0]);
        assertEquals(5000, havingData[1]);
    }

    @Test
    @DisplayName("测试OR逻辑操作符")
    void testOr() {
        wrapper.or();
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("", condition.getField());
        assertEquals(QueryOperator.OR, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试AND逻辑操作符")
    void testAnd() {
        wrapper.and();
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("", condition.getField());
        assertEquals(QueryOperator.AND, condition.getOperator());
        assertNull(condition.getValue());
    }

    @Test
    @DisplayName("测试嵌套查询")
    void testNest() {
        LambdaQueryWrapper<TestUser> nestedWrapper = LambdaQueryWrapper.of(TestUser.class)
                .eq(TestUser::getAge, 30);
        
        wrapper.nest(nestedWrapper);
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("", condition.getField());
        assertEquals(QueryOperator.NEST, condition.getOperator());
        assertEquals(nestedWrapper, condition.getValue());
    }

    @Test
    @DisplayName("测试自定义条件")
    void testApply() {
        wrapper.apply("DATE(create_time) = ?", "2024-01-01");
        
        assertEquals(1, wrapper.size());
        QueryCondition condition = wrapper.getConditions().get(0);
        assertEquals("", condition.getField());
        assertEquals(QueryOperator.APPLY, condition.getOperator());
        
        Object[] applyData = (Object[]) condition.getValue();
        assertEquals("DATE(create_time) = ?", applyData[0]);
        assertEquals("2024-01-01", applyData[1]);
    }

    @Test
    @DisplayName("测试链式调用")
    void testChaining() {
        wrapper.eq(TestUser::getName, "张三")
               .gt(TestUser::getAge, 18)
               .like(TestUser::getEmail, "@gmail.com")
               .orderByDesc(TestUser::getCreateTime);
        
        assertEquals(4, wrapper.size());
        
        QueryCondition condition1 = wrapper.getConditions().get(0);
        assertEquals("name", condition1.getField());
        assertEquals(QueryOperator.EQ, condition1.getOperator());
        
        QueryCondition condition2 = wrapper.getConditions().get(1);
        assertEquals("age", condition2.getField());
        assertEquals(QueryOperator.GT, condition2.getOperator());
        
        QueryCondition condition3 = wrapper.getConditions().get(2);
        assertEquals("email", condition3.getField());
        assertEquals(QueryOperator.LIKE, condition3.getOperator());
        
        QueryCondition condition4 = wrapper.getConditions().get(3);
        assertEquals("createTime", condition4.getField());
        assertEquals(QueryOperator.ORDER_BY_DESC, condition4.getOperator());
    }

    @Test
    @DisplayName("测试清空条件")
    void testClear() {
        wrapper.eq(TestUser::getName, "张三")
               .gt(TestUser::getAge, 18);
        
        assertEquals(2, wrapper.size());
        
        wrapper.clear();
        
        assertEquals(0, wrapper.size());
        assertTrue(wrapper.isEmpty());
    }

    @Test
    @DisplayName("测试添加条件")
    void testAddCondition() {
        QueryCondition condition = new QueryCondition("name", QueryOperator.EQ, "张三");
        wrapper.addCondition(condition);
        
        assertEquals(1, wrapper.size());
        assertEquals(condition, wrapper.getConditions().get(0));
    }

    @Test
    @DisplayName("测试添加空条件")
    void testAddNullCondition() {
        wrapper.addCondition(null);
        
        assertEquals(0, wrapper.size());
        assertTrue(wrapper.isEmpty());
    }

    /**
     * 测试用的用户实体类
     */
    static class TestUser {
        private String name;
        private Integer age;
        private String email;
        private String phone;
        private String department;
        private String role;
        private Integer salary;
        private String createTime;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public Integer getSalary() { return salary; }
        public void setSalary(Integer salary) { this.salary = salary; }
        
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
    }
} 