package top.ysqorz.mybatis.config;

import top.ysqorz.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * 全局环境
 *
 * @author passerbyYSQ
 * @create 2022-06-07 16:40
 */
public class Environment {
    // 环境id
    private String id;
    private TransactionFactory transactionFactory;
    private DataSource dataSource;

    /**
     * 私有化构造方法，只能通过Builder创建Environment实例
     */
    private Environment() {}

    public static class Builder {
        private Environment env = new Environment();

        public Builder(String envId) {
            env.id = envId;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            env.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            env.dataSource = dataSource;
            return this;
        }

        public Environment build() {
            assert env.id != null;
            assert env.transactionFactory != null;
            assert env.dataSource != null;
            return env;
        }
    }

    public String getId() {
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
