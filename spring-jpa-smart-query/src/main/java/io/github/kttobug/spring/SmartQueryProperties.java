package io.github.kttobug.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Smart Query 配置属性
 * 
 * <p>用于配置 Smart Query 的各种行为</p>
 * 
 * @author kttobug
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "smart-query")
public class SmartQueryProperties {
    
    /**
     * 是否启用性能监控
     */
    private boolean enablePerformanceMonitor = true;
    
    /**
     * 是否启用查询日志
     */
    private boolean enableQueryLog = false;
    
    /**
     * 查询超时时间（毫秒）
     */
    private long queryTimeout = 30000;
    
    /**
     * 是否启用自动配置
     */
    private boolean enabled = true;
    
    public boolean isEnablePerformanceMonitor() {
        return enablePerformanceMonitor;
    }
    
    public void setEnablePerformanceMonitor(boolean enablePerformanceMonitor) {
        this.enablePerformanceMonitor = enablePerformanceMonitor;
    }
    
    public boolean isEnableQueryLog() {
        return enableQueryLog;
    }
    
    public void setEnableQueryLog(boolean enableQueryLog) {
        this.enableQueryLog = enableQueryLog;
    }
    
    public long getQueryTimeout() {
        return queryTimeout;
    }
    
    public void setQueryTimeout(long queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
} 