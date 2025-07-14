package io.github.kttobug.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 查询性能监控器
 * 
 * <p>用于监控查询性能，包括执行时间、执行次数等统计信息。</p>
 * 
 * @author kttobug
 * @since 1.0.0
 */
@Component
public class QueryPerformanceMonitor {
    
    private static final Logger logger = LoggerFactory.getLogger(QueryPerformanceMonitor.class);
    
    /**
     * 查询统计信息
     */
    private static class QueryStats {
        private final AtomicLong executionCount = new AtomicLong(0);
        private final AtomicLong totalExecutionTime = new AtomicLong(0);
        private volatile long maxExecutionTime = 0;
        private volatile long minExecutionTime = Long.MAX_VALUE;
        
        public void recordExecution(long executionTime) {
            executionCount.incrementAndGet();
            totalExecutionTime.addAndGet(executionTime);
            
            if (executionTime > maxExecutionTime) {
                maxExecutionTime = executionTime;
        }
        
            if (executionTime < minExecutionTime) {
                minExecutionTime = executionTime;
    }
        }
        
        public double getAverageExecutionTime() {
            long count = executionCount.get();
            return count > 0 ? (double) totalExecutionTime.get() / count : 0;
        }
        
        public long getExecutionCount() {
            return executionCount.get();
        }
        
        public long getTotalExecutionTime() {
            return totalExecutionTime.get();
        }
        
        public long getMaxExecutionTime() {
            return maxExecutionTime;
        }
        
        public long getMinExecutionTime() {
            return minExecutionTime == Long.MAX_VALUE ? 0 : minExecutionTime;
    }
    }
    
    private final ConcurrentHashMap<String, QueryStats> queryStatsMap = new ConcurrentHashMap<>();

    /**
     * 记录查询开始时间
     * 
     * @param queryKey 查询键
     * @return 开始时间戳
     */
    public long startQuery(String queryKey) {
        return System.currentTimeMillis();
    }

    /**
     * 记录查询结束时间并计算执行时间
     * 
     * @param queryKey 查询键
     * @param startTime 开始时间戳
     */
    public void endQuery(String queryKey, long startTime) {
        long executionTime = System.currentTimeMillis() - startTime;
        recordQueryExecution(queryKey, executionTime);
    }

    /**
     * 记录查询执行信息
     * 
     * @param queryKey 查询键
     * @param executionTime 执行时间
     */
    public void recordQueryExecution(String queryKey, long executionTime) {
        QueryStats stats = queryStatsMap.computeIfAbsent(queryKey, k -> new QueryStats());
        stats.recordExecution(executionTime);
        
        // 如果执行时间过长，记录警告日志
        if (executionTime > 1000) {
            logger.warn("Slow query detected: {} took {}ms", queryKey, executionTime);
        }
    }

    /**
     * 获取查询统计信息
     * 
     * @param queryKey 查询键
     * @return 统计信息
     */
    public QueryStatistics getQueryStatistics(String queryKey) {
        QueryStats stats = queryStatsMap.get(queryKey);
        if (stats == null) {
            return new QueryStatistics(queryKey, 0, 0, 0, 0, 0);
        }

        return new QueryStatistics(
            queryKey,
            stats.getExecutionCount(),
            stats.getTotalExecutionTime(),
            stats.getAverageExecutionTime(),
            stats.getMaxExecutionTime(),
            stats.getMinExecutionTime()
        );
    }

    /**
     * 获取所有查询统计信息
     * 
     * @return 所有统计信息
     */
    public java.util.Map<String, QueryStatistics> getAllQueryStatistics() {
        java.util.Map<String, QueryStatistics> result = new java.util.HashMap<>();
        
        for (java.util.Map.Entry<String, QueryStats> entry : queryStatsMap.entrySet()) {
            String queryKey = entry.getKey();
            QueryStats stats = entry.getValue();
            
            result.put(queryKey, new QueryStatistics(
                queryKey,
                stats.getExecutionCount(),
                stats.getTotalExecutionTime(),
                stats.getAverageExecutionTime(),
                stats.getMaxExecutionTime(),
                stats.getMinExecutionTime()
            ));
        }
        
        return result;
        }

        /**
     * 清空所有统计信息
         */
    public void clearStatistics() {
        queryStatsMap.clear();
        }

        /**
     * 打印性能报告
         */
    public void printPerformanceReport() {
        logger.info("=== Query Performance Report ===");
        
        for (java.util.Map.Entry<String, QueryStats> entry : queryStatsMap.entrySet()) {
            String queryKey = entry.getKey();
            QueryStats stats = entry.getValue();
            
            logger.info("Query: {}", queryKey);
            logger.info("  Execution Count: {}", stats.getExecutionCount());
            logger.info("  Total Time: {}ms", stats.getTotalExecutionTime());
            logger.info("  Average Time: {:.2f}ms", stats.getAverageExecutionTime());
            logger.info("  Max Time: {}ms", stats.getMaxExecutionTime());
            logger.info("  Min Time: {}ms", stats.getMinExecutionTime());
            logger.info("  ---");
        }
        }

        /**
     * 查询统计信息数据类
         */
    public static class QueryStatistics {
        private final String queryKey;
        private final long executionCount;
        private final long totalExecutionTime;
        private final double averageExecutionTime;
        private final long maxExecutionTime;
        private final long minExecutionTime;
        
        public QueryStatistics(String queryKey, long executionCount, long totalExecutionTime, 
                             double averageExecutionTime, long maxExecutionTime, long minExecutionTime) {
            this.queryKey = queryKey;
            this.executionCount = executionCount;
            this.totalExecutionTime = totalExecutionTime;
            this.averageExecutionTime = averageExecutionTime;
            this.maxExecutionTime = maxExecutionTime;
            this.minExecutionTime = minExecutionTime;
        }
        
        public String getQueryKey() {
            return queryKey;
        }

        public long getExecutionCount() {
            return executionCount;
        }

        public long getTotalExecutionTime() {
            return totalExecutionTime;
        }
        
        public double getAverageExecutionTime() {
            return averageExecutionTime;
        }
        
        public long getMaxExecutionTime() {
            return maxExecutionTime;
        }
        
        public long getMinExecutionTime() {
            return minExecutionTime;
        }
    }
} 