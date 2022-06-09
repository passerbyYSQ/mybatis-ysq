package top.ysqorz.mybatis.transaction;

import java.sql.Connection;

/**
 * 事务的隔离级别
 *
 * @author passerbyYSQ
 * @create 2022-06-06 19:33
 */
public enum TransactionIsolationLevel {

    NONE(Connection.TRANSACTION_NONE),

    /**
     * 读未提交
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

    /**
     * 读已提交
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    /**
     * 可重复读
     */
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    /**
     * 串行化
     */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private final int level;

    TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
