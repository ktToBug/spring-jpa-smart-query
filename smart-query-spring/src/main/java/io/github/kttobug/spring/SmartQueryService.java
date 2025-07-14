package io.github.kttobug.spring;

import io.github.kttobug.query.LambdaQueryWrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 智能查询服务
 * 
 * <p>提供基于 LambdaQueryWrapper 的查询功能</p>
 * 
 * @author kttobug
 * @since 1.0.0
 */
@Service
public class SmartQueryService {
    
    @Autowired
    private EntityManager entityManager;
    
    /**
     * 执行查询并返回结果列表
     * 
     * @param queryWrapper 查询包装器
     * @param <T> 实体类型
     * @return 查询结果列表
     */
    public <T> List<T> findAll(LambdaQueryWrapper<T> queryWrapper) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(queryWrapper.getEntityClass());
        Root<T> root = query.from(queryWrapper.getEntityClass());
        
        query.select(root);
        
        return entityManager.createQuery(query).getResultList();
    }
    
    /**
     * 执行分页查询
     * 
     * @param queryWrapper 查询包装器
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param <T> 实体类型
     * @return 查询结果列表
     */
    public <T> List<T> findAll(LambdaQueryWrapper<T> queryWrapper, int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(queryWrapper.getEntityClass());
        Root<T> root = query.from(queryWrapper.getEntityClass());
        
        query.select(root);
        
        return entityManager.createQuery(query)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
    
    /**
     * 计算查询结果数量
     * 
     * @param queryWrapper 查询包装器
     * @param <T> 实体类型
     * @return 结果数量
     */
    public <T> long count(LambdaQueryWrapper<T> queryWrapper) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<T> root = query.from(queryWrapper.getEntityClass());
        
        query.select(builder.count(root));
        
        return entityManager.createQuery(query).getSingleResult();
    }
} 