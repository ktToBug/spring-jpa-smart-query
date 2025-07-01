package io.github.kttobug.spring;

import io.github.kttobug.query.LambdaQueryWrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

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

        Predicate[] predicates = queryWrapper.getConditions().stream()
                .map(condition -> {
                    switch (condition.getOperator()) {
                        case EQ:
                            return builder.equal(root.get(condition.getField()), condition.getValue());
                        case LIKE:
                            return builder.like(root.get(condition.getField()), "%" + condition.getValue() + "%");
                        // 其他操作符...
                        default:
                            throw new UnsupportedOperationException("Operator not supported: " + condition.getOperator());
                    }
                })
                .toArray(Predicate[]::new);

        return query.where(predicates);
    }
}