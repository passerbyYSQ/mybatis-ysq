package top.ysqorz.mybatis.transaction;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 事务工厂(事务管理器)
 *
 * @author passerbyYSQ
 * @create 2022-06-06 19:26
 */
public interface TransactionFactory {

    /**
     * 根据数据库连接创建事务
     */
    Transaction newTransaction(Connection conn);

    /**
     * 根据数据源和事务隔离级别创建事务
     *
     * @param dataSource    数据源
     * @param level         事务隔离级别
     * @param autoCommit    是否自动提交事务
     * @return              事务
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);
}
