package top.ysqorz.mybatis.transaction.jdbc;

import top.ysqorz.mybatis.transaction.Transaction;
import top.ysqorz.mybatis.transaction.TransactionFactory;
import top.ysqorz.mybatis.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author passerbyYSQ
 * @create 2022-06-07 14:02
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
