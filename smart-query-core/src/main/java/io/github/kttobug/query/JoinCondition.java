package io.github.kttobug.query;

/**
 * JOIN 查询条件类，用于定义表连接关系。
 * 
 * <p>该类用于构建复杂的 JOIN 查询，支持多种连接类型：</p>
 * <ul>
 *   <li>INNER JOIN - 内连接</li>
 *   <li>LEFT JOIN - 左连接</li>
 *   <li>RIGHT JOIN - 右连接</li>
 *   <li>FULL JOIN - 全连接</li>
 * </ul>
 * 
 * @author kttobug
 * @since 1.0.0
 */
public class JoinCondition {
    
    /** 连接类型枚举 */
    public enum JoinType {
        /** 内连接 */
        INNER,
        /** 左连接 */
        LEFT,
        /** 右连接 */
        RIGHT,
        /** 全连接 */
        FULL
    }
    
    /** 连接类型 */
    private final JoinType joinType;
    
    /** 目标实体类 */
    private final Class<?> targetEntity;
    
    /** 别名 */
    private final String alias;
    
    /** 连接条件 */
    private final String joinCondition;

    /**
     * 构造函数
     * 
     * @param joinType 连接类型
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     */
    public JoinCondition(JoinType joinType, Class<?> targetEntity, String alias, String joinCondition) {
        this.joinType = joinType;
        this.targetEntity = targetEntity;
        this.alias = alias;
        this.joinCondition = joinCondition;
    }

    /**
     * 创建内连接条件
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return JOIN 条件
     */
    public static JoinCondition innerJoin(Class<?> targetEntity, String alias, String joinCondition) {
        return new JoinCondition(JoinType.INNER, targetEntity, alias, joinCondition);
    }

    /**
     * 创建左连接条件
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return JOIN 条件
     */
    public static JoinCondition leftJoin(Class<?> targetEntity, String alias, String joinCondition) {
        return new JoinCondition(JoinType.LEFT, targetEntity, alias, joinCondition);
    }

    /**
     * 创建右连接条件
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return JOIN 条件
     */
    public static JoinCondition rightJoin(Class<?> targetEntity, String alias, String joinCondition) {
        return new JoinCondition(JoinType.RIGHT, targetEntity, alias, joinCondition);
    }

    /**
     * 创建全连接条件
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return JOIN 条件
     */
    public static JoinCondition fullJoin(Class<?> targetEntity, String alias, String joinCondition) {
        return new JoinCondition(JoinType.FULL, targetEntity, alias, joinCondition);
    }

    /**
     * 获取连接类型
     * 
     * @return 连接类型
     */
    public JoinType getJoinType() {
        return joinType;
    }

    /**
     * 获取目标实体类
     * 
     * @return 目标实体类
     */
    public Class<?> getTargetEntity() {
        return targetEntity;
    }

    /**
     * 获取别名
     * 
     * @return 别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 获取连接条件
     * 
     * @return 连接条件
     */
    public String getJoinCondition() {
        return joinCondition;
    }

    @Override
    public String toString() {
        return String.format("JoinCondition{type=%s, target=%s, alias='%s', condition='%s'}",
                joinType, targetEntity.getSimpleName(), alias, joinCondition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinCondition that = (JoinCondition) o;

        if (joinType != that.joinType) return false;
        if (!targetEntity.equals(that.targetEntity)) return false;
        if (!alias.equals(that.alias)) return false;
        return joinCondition.equals(that.joinCondition);
    }

    @Override
    public int hashCode() {
        int result = joinType.hashCode();
        result = 31 * result + targetEntity.hashCode();
        result = 31 * result + alias.hashCode();
        result = 31 * result + joinCondition.hashCode();
        return result;
    }
} 