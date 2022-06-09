package top.ysqorz.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务
 *
 * @author passerbyYSQ
 * @create 2022-06-06 19:27
 */
public interface Transaction {

    /**
     * 获取数据库连接
     */
    Connection getConnection() throws SQLException;

    /**
     * 提交事务
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     */
    void rollback() throws SQLException;

    /**
     * 关闭事务
     */
    void close() throws SQLException;
}
