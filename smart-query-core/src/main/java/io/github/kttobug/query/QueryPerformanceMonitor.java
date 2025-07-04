package io.github.kttobug.query;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 查询性能监控器，用于监控查询执行时间和统计信息。
 * 
 * <p>该类提供了查询性能的监控功能，包括：</p>
 * <ul>
 *   <li>查询执行时间统计</li>
 *   <li>查询次数统计</li>
 *   <li>慢查询检测</li>
 *   <li>性能指标收集</li>
 * </ul>
 * 
 * @author kttobug
 * @since 1.0.0
 */
public class QueryPerformanceMonitor {
    
    /** 慢查询阈值（毫秒） */
    private static final long SLOW_QUERY_THRESHOLD = 1000L;
    
    /** 查询统计信息 */
    private static final ConcurrentHashMap<String, QueryStats> queryStats = new ConcurrentHashMap<>();
    
    /** 总查询次数 */
    private static final LongAdder totalQueryCount = new LongAdder();
    
    /** 总查询时间 */
    private static final LongAdder totalQueryTime = new LongAdder();
    
    /** 慢查询次数 */
    private static final LongAdder slowQueryCount = new LongAdder();

    /**
     * 开始监控查询
     * 
     * @param queryType 查询类型标识
     * @return 查询监控上下文
     */
    public static QueryContext startMonitoring(String queryType) {
        return new QueryContext(queryType, System.currentTimeMillis());
    }

    /**
     * 结束监控查询
     * 
     * @param context 查询监控上下文
     */
    public static void endMonitoring(QueryContext context) {
        long executionTime = System.currentTimeMillis() - context.getStartTime();
        
        // 更新总统计
        totalQueryCount.increment();
        totalQueryTime.add(executionTime);
        
        // 检查是否为慢查询
        if (executionTime > SLOW_QUERY_THRESHOLD) {
            slowQueryCount.increment();
            logSlowQuery(context.getQueryType(), executionTime);
        }
        
        // 更新查询类型统计
        queryStats.computeIfAbsent(context.getQueryType(), k -> new QueryStats())
                 .recordQuery(executionTime);
    }

    /**
     * 获取查询统计信息
     * 
     * @param queryType 查询类型，如果为 null 则返回所有统计
     * @return 查询统计信息
     */
    public static QueryStats getStats(String queryType) {
        if (queryType == null) {
            return getOverallStats();
        }
        return queryStats.getOrDefault(queryType, new QueryStats());
    }

    /**
     * 获取总体统计信息
     * 
     * @return 总体统计信息
     */
    public static QueryStats getOverallStats() {
        QueryStats overall = new QueryStats();
        overall.setTotalCount(totalQueryCount.sum());
        overall.setTotalTime(totalQueryTime.sum());
        overall.setSlowQueryCount(slowQueryCount.sum());
        
        if (overall.getTotalCount() > 0) {
            overall.setAverageTime(overall.getTotalTime() / overall.getTotalCount());
        }
        
        return overall;
    }

    /**
     * 重置所有统计信息
     */
    public static void resetStats() {
        queryStats.clear();
        totalQueryCount.reset();
        totalQueryTime.reset();
        slowQueryCount.reset();
    }

    /**
     * 获取所有查询类型的统计信息
     * 
     * @return 查询类型统计信息映射
     */
    public static ConcurrentHashMap<String, QueryStats> getAllStats() {
        return new ConcurrentHashMap<>(queryStats);
    }

    /**
     * 设置慢查询阈值
     * 
     * @param threshold 阈值（毫秒）
     */
    public static void setSlowQueryThreshold(long threshold) {
        // 这里可以通过配置来动态设置阈值
    }

    /**
     * 记录慢查询日志
     * 
     * @param queryType 查询类型
     * @param executionTime 执行时间
     */
    private static void logSlowQuery(String queryType, long executionTime) {
        System.err.printf("[SLOW QUERY] Type: %s, Time: %dms%n", queryType, executionTime);
    }

    /**
     * 查询监控上下文
     */
    public static class QueryContext {
        private final String queryType;
        private final long startTime;

        public QueryContext(String queryType, long startTime) {
            this.queryType = queryType;
            this.startTime = startTime;
        }

        public String getQueryType() {
            return queryType;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    /**
     * 查询统计信息
     */
    public static class QueryStats {
        private final AtomicLong totalCount = new AtomicLong(0);
        private final AtomicLong totalTime = new AtomicLong(0);
        private final AtomicLong minTime = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxTime = new AtomicLong(0);
        private final AtomicLong slowQueryCount = new AtomicLong(0);

        /**
         * 记录一次查询
         * 
         * @param executionTime 执行时间
         */
        public void recordQuery(long executionTime) {
            totalCount.incrementAndGet();
            totalTime.addAndGet(executionTime);
            
            // 更新最小时间
            minTime.updateAndGet(current -> Math.min(current, executionTime));
            
            // 更新最大时间
            maxTime.updateAndGet(current -> Math.max(current, executionTime));
            
            // 检查是否为慢查询
            if (executionTime > SLOW_QUERY_THRESHOLD) {
                slowQueryCount.incrementAndGet();
            }
        }

        /**
         * 获取总查询次数
         * 
         * @return 总查询次数
         */
        public long getTotalCount() {
            return totalCount.get();
        }

        /**
         * 设置总查询次数
         * 
         * @param totalCount 总查询次数
         */
        public void setTotalCount(long totalCount) {
            this.totalCount.set(totalCount);
        }

        /**
         * 获取总查询时间
         * 
         * @return 总查询时间（毫秒）
         */
        public long getTotalTime() {
            return totalTime.get();
        }

        /**
         * 设置总查询时间
         * 
         * @param totalTime 总查询时间
         */
        public void setTotalTime(long totalTime) {
            this.totalTime.set(totalTime);
        }

        /**
         * 获取平均查询时间
         * 
         * @return 平均查询时间（毫秒）
         */
        public long getAverageTime() {
            long count = totalCount.get();
            return count > 0 ? totalTime.get() / count : 0;
        }

        /**
         * 设置平均查询时间
         * 
         * @param averageTime 平均查询时间
         */
        public void setAverageTime(long averageTime) {
            // 这是一个计算值，通常不需要设置
        }

        /**
         * 获取最小查询时间
         * 
         * @return 最小查询时间（毫秒）
         */
        public long getMinTime() {
            long min = minTime.get();
            return min == Long.MAX_VALUE ? 0 : min;
        }

        /**
         * 获取最大查询时间
         * 
         * @return 最大查询时间（毫秒）
         */
        public long getMaxTime() {
            return maxTime.get();
        }

        /**
         * 获取慢查询次数
         * 
         * @return 慢查询次数
         */
        public long getSlowQueryCount() {
            return slowQueryCount.get();
        }

        /**
         * 设置慢查询次数
         * 
         * @param slowQueryCount 慢查询次数
         */
        public void setSlowQueryCount(long slowQueryCount) {
            this.slowQueryCount.set(slowQueryCount);
        }

        @Override
        public String toString() {
            return String.format(
                "QueryStats{totalCount=%d, totalTime=%dms, avgTime=%dms, minTime=%dms, maxTime=%dms, slowQueries=%d}",
                getTotalCount(), getTotalTime(), getAverageTime(), getMinTime(), getMaxTime(), getSlowQueryCount()
            );
        }
    }
} 