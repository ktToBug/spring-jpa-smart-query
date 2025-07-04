package io.github.kttobug.spring;

import io.github.kttobug.query.LambdaQueryWrapper;
import io.github.kttobug.query.QueryCondition;
import io.github.kttobug.query.QueryOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LambdaQueryExecutorImpl<T> extends SimpleJpaRepository<T, Long> implements LambdaQueryExecutor<T> {

    private final EntityManager entityManager;

    public LambdaQueryExecutorImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public LambdaQueryExecutorImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<T> list(LambdaQueryWrapper<T> queryWrapper) {
        return entityManager.createQuery(buildCriteriaQuery(queryWrapper)).getResultList();
    }

    private CriteriaQuery<T> buildCriteriaQuery(LambdaQueryWrapper<T> queryWrapper) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(queryWrapper.getEntityClass());
        Root<T> root = query.from(queryWrapper.getEntityClass());

        QueryContext context = new QueryContext(builder, root);

        for (QueryCondition condition : queryWrapper.getConditions()) {
            processCondition(condition, context);
        }

        // 应用 WHERE 条件
        if (!context.getPredicates().isEmpty()) {
            query.where(context.getPredicates().toArray(new Predicate[0]));
        }

        // 应用 GROUP BY
        if (!context.getGroupByExpressions().isEmpty()) {
            query.groupBy(context.getGroupByExpressions());
        }

        // 应用 ORDER BY
        if (!context.getOrders().isEmpty()) {
            query.orderBy(context.getOrders());
        }

        return query;
    }

    private void processCondition(QueryCondition condition, QueryContext context) {
        switch (condition.getOperator()) {
            case EQ:
                context.addPredicate(buildEqualPredicate(condition, context));
                break;
            case NE:
                context.addPredicate(buildNotEqualPredicate(condition, context));
                break;
            case GT:
                context.addPredicate(buildGreaterThanPredicate(condition, context));
                break;
            case GE:
                context.addPredicate(buildGreaterThanOrEqualPredicate(condition, context));
                break;
            case LT:
                context.addPredicate(buildLessThanPredicate(condition, context));
                break;
            case LE:
                context.addPredicate(buildLessThanOrEqualPredicate(condition, context));
                break;
            case LIKE:
                context.addPredicate(buildLikePredicate(condition, context, "%", "%"));
                break;
            case LEFT_LIKE:
                context.addPredicate(buildLikePredicate(condition, context, "%", ""));
                break;
            case RIGHT_LIKE:
                context.addPredicate(buildLikePredicate(condition, context, "", "%"));
                break;
            case IN:
                context.addPredicate(buildInPredicate(condition, context));
                break;
            case NOT_IN:
                context.addPredicate(buildNotInPredicate(condition, context));
                break;
            case IS_NULL:
                context.addPredicate(buildIsNullPredicate(condition, context));
                break;
            case IS_NOT_NULL:
                context.addPredicate(buildIsNotNullPredicate(condition, context));
                break;
            case BETWEEN:
                context.addPredicate(buildBetweenPredicate(condition, context, false));
                break;
            case NOT_BETWEEN:
                context.addPredicate(buildBetweenPredicate(condition, context, true));
                break;
            case ORDER_BY_ASC:
                context.addOrder(buildAscOrder(condition, context));
                break;
            case ORDER_BY_DESC:
                context.addOrder(buildDescOrder(condition, context));
                break;
            case GROUP_BY:
                context.addGroupBy(buildGroupByExpression(condition, context));
                break;
            case HAVING:
                processHavingCondition(condition, context);
                break;
            case OR:
                processOrCondition(condition, context);
                break;
            case AND:
                processAndCondition(condition, context);
                break;
            case NEST:
                processNestCondition(condition, context);
                break;
            case APPLY:
                processApplyCondition(condition, context);
                break;
            default:
                throw new UnsupportedOperationException("Operator not supported: " + condition.getOperator());
        }
    }

    // 查询上下文类，用于存储查询构建过程中的状态
    private static class QueryContext {
        private final CriteriaBuilder builder;
        private final Root<?> root;
        private final List<Predicate> predicates = new ArrayList<>();
        private final List<Order> orders = new ArrayList<>();
        private final List<Expression<?>> groupByExpressions = new ArrayList<>();

        public QueryContext(CriteriaBuilder builder, Root<?> root) {
            this.builder = builder;
            this.root = root;
        }

        public CriteriaBuilder getBuilder() {
            return builder;
        }

        public Root<?> getRoot() {
            return root;
        }

        public List<Predicate> getPredicates() {
            return predicates;
        }

        public List<Order> getOrders() {
            return orders;
        }

        public List<Expression<?>> getGroupByExpressions() {
            return groupByExpressions;
        }

        public void addPredicate(Predicate predicate) {
            if (predicate != null) {
                predicates.add(predicate);
            }
        }

        public void addOrder(Order order) {
            if (order != null) {
                orders.add(order);
            }
        }

        public void addGroupBy(Expression<?> expression) {
            if (expression != null) {
                groupByExpressions.add(expression);
            }
        }
    }

    // 构建各种谓词的方法
    private Predicate buildEqualPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().equal(context.getRoot().get(condition.getField()), condition.getValue());
    }

    private Predicate buildNotEqualPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().notEqual(context.getRoot().get(condition.getField()), condition.getValue());
    }

    private Predicate buildGreaterThanPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().greaterThan(context.getRoot().get(condition.getField()), (Comparable) condition.getValue());
    }

    private Predicate buildGreaterThanOrEqualPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().greaterThanOrEqualTo(context.getRoot().get(condition.getField()), (Comparable) condition.getValue());
    }

    private Predicate buildLessThanPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().lessThan(context.getRoot().get(condition.getField()), (Comparable) condition.getValue());
    }

    private Predicate buildLessThanOrEqualPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().lessThanOrEqualTo(context.getRoot().get(condition.getField()), (Comparable) condition.getValue());
    }

    private Predicate buildLikePredicate(QueryCondition condition, QueryContext context, String prefix, String suffix) {
        if (condition.getValue() instanceof Collection) {
            Collection<?> values = (Collection<?>) condition.getValue();
            return context.getBuilder().or(values.stream()
                    .map(value -> context.getBuilder().like(context.getRoot().get(condition.getField()), prefix + value + suffix)).toArray(Predicate[]::new));
        } else {
            return context.getBuilder().like(context.getRoot().get(condition.getField()), prefix + condition.getValue() + suffix);
        }
    }

    private Predicate buildInPredicate(QueryCondition condition, QueryContext context) {
        if (condition.getValue() instanceof Collection) {
            Collection<?> values = (Collection<?>) condition.getValue();
            return context.getRoot().get(condition.getField()).in(values);
        }
        return null;
    }

    private Predicate buildNotInPredicate(QueryCondition condition, QueryContext context) {
        if (condition.getValue() instanceof Collection) {
            Collection<?> values = (Collection<?>) condition.getValue();
            return context.getBuilder().not(context.getRoot().get(condition.getField()).in(values));
        }
        return null;
    }

    private Predicate buildIsNullPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().isNull(context.getRoot().get(condition.getField()));
    }

    private Predicate buildIsNotNullPredicate(QueryCondition condition, QueryContext context) {
        return context.getBuilder().isNotNull(context.getRoot().get(condition.getField()));
    }

    private Predicate buildBetweenPredicate(QueryCondition condition, QueryContext context, boolean negated) {
        if (condition.getValue() instanceof Collection) {
            Collection<?> values = (Collection<?>) condition.getValue();
            Object[] array = values.toArray();
            if (array.length >= 2) {
                Predicate betweenPredicate = context.getBuilder().between(
                        context.getRoot().get(condition.getField()),
                        (Comparable) array[0],
                        (Comparable) array[1]
                );
                return negated ? context.getBuilder().not(betweenPredicate) : betweenPredicate;
            }
        }
        return null;
    }

    // 构建排序和分组的方法
    private Order buildAscOrder(QueryCondition condition, QueryContext context) {
        return context.getBuilder().asc(context.getRoot().get(condition.getField()));
    }

    private Order buildDescOrder(QueryCondition condition, QueryContext context) {
        return context.getBuilder().desc(context.getRoot().get(condition.getField()));
    }

    private Expression<?> buildGroupByExpression(QueryCondition condition, QueryContext context) {
        return context.getRoot().get(condition.getField());
    }

    // 处理特殊条件的方法
    private void processHavingCondition(QueryCondition condition, QueryContext context) {
        // HAVING 条件需要特殊处理，这里简化处理
        Object[] havingData = (Object[]) condition.getValue();
        if (havingData != null && havingData.length >= 2) {
            QueryOperator havingOperator = (QueryOperator) havingData[0];
            Object havingValue = havingData[1];
            // 这里需要根据具体需求实现 HAVING 逻辑
            // 由于 JPA Criteria API 对 HAVING 支持有限，可能需要使用原生 SQL
        }
    }

    private void processOrCondition(QueryCondition condition, QueryContext context) {
        // OR 操作符需要特殊处理，这里简化处理
        // 实际实现中需要更复杂的逻辑来处理 OR 条件
    }

    private void processAndCondition(QueryCondition condition, QueryContext context) {
        // AND 操作符是默认行为，不需要特殊处理
    }

    private void processNestCondition(QueryCondition condition, QueryContext context) {
        // 嵌套查询需要特殊处理
        if (condition.getValue() instanceof LambdaQueryWrapper) {
            LambdaQueryWrapper<T> nestedWrapper = (LambdaQueryWrapper<T>) condition.getValue();
            // 这里需要递归处理嵌套查询
            // 简化实现：暂时跳过
        }
    }

    private void processApplyCondition(QueryCondition condition, QueryContext context) {
        // 自定义条件，需要特殊处理
        Object[] applyData = (Object[]) condition.getValue();
        if (applyData != null && applyData.length >= 1) {
            String customCondition = (String) applyData[0];
            // 这里需要解析自定义条件并转换为 Predicate
            // 由于复杂性，这里暂时跳过
        }
    }

    // 添加分页查询方法
    public List<T> listWithPagination(LambdaQueryWrapper<T> queryWrapper, int page, int size) {
        CriteriaQuery<T> criteriaQuery = buildCriteriaQuery(queryWrapper);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    // 添加计数查询方法
    public long count(LambdaQueryWrapper<T> queryWrapper) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(queryWrapper.getEntityClass());

        query.select(builder.count(root));

        List<Predicate> predicates = buildPredicates(queryWrapper, builder, root);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(query).getSingleResult();
    }

    // 提取构建 Predicate 的逻辑
    private List<Predicate> buildPredicates(LambdaQueryWrapper<T> queryWrapper, CriteriaBuilder builder, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();

        for (QueryCondition condition : queryWrapper.getConditions()) {
            // 只处理 WHERE 条件，跳过排序、分组等
            if (isWhereCondition(condition.getOperator())) {
                Predicate predicate = buildPredicate(condition, builder, root);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }

        return predicates;
    }

    private boolean isWhereCondition(QueryOperator operator) {
        return operator != QueryOperator.ORDER_BY_ASC &&
                operator != QueryOperator.ORDER_BY_DESC &&
                operator != QueryOperator.GROUP_BY;
    }

    private Predicate buildPredicate(QueryCondition condition, CriteriaBuilder builder, Root<T> root) {
                    switch (condition.getOperator()) {
                        case EQ:
                            return builder.equal(root.get(condition.getField()), condition.getValue());
            case NE:
                return builder.notEqual(root.get(condition.getField()), condition.getValue());
            case GT:
                return builder.greaterThan(root.get(condition.getField()), (Comparable) condition.getValue());
            case GE:
                return builder.greaterThanOrEqualTo(root.get(condition.getField()), (Comparable) condition.getValue());
            case LT:
                return builder.lessThan(root.get(condition.getField()), (Comparable) condition.getValue());
            case LE:
                return builder.lessThanOrEqualTo(root.get(condition.getField()), (Comparable) condition.getValue());
                        case LIKE:
                            return builder.like(root.get(condition.getField()), "%" + condition.getValue() + "%");
            case LEFT_LIKE:
                return builder.like(root.get(condition.getField()), "%" + condition.getValue());
            case RIGHT_LIKE:
                return builder.like(root.get(condition.getField()), condition.getValue() + "%");
            case IN:
                if (condition.getValue() instanceof Collection) {
                    return root.get(condition.getField()).in((Collection<?>) condition.getValue());
                }
                break;
            case NOT_IN:
                if (condition.getValue() instanceof Collection) {
                    return builder.not(root.get(condition.getField()).in((Collection<?>) condition.getValue()));
                }
                break;
            case IS_NULL:
                return builder.isNull(root.get(condition.getField()));
            case IS_NOT_NULL:
                return builder.isNotNull(root.get(condition.getField()));
            case BETWEEN:
                if (condition.getValue() instanceof Collection) {
                    Collection<?> values = (Collection<?>) condition.getValue();
                    Object[] array = values.toArray();
                    if (array.length >= 2) {
                        return builder.between(root.get(condition.getField()),
                                (Comparable) array[0], (Comparable) array[1]);
                    }
                }
                break;
            case NOT_BETWEEN:
                if (condition.getValue() instanceof Collection) {
                    Collection<?> values = (Collection<?>) condition.getValue();
                    Object[] array = values.toArray();
                    if (array.length >= 2) {
                        return builder.not(builder.between(root.get(condition.getField()),
                                (Comparable) array[0], (Comparable) array[1]));
                    }
                }
                break;
        }
        return null;
    }
}