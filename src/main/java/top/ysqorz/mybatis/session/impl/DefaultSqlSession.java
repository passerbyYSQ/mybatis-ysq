package top.ysqorz.mybatis.session.impl;

import top.ysqorz.mybatis.mapping.MappedStatement;
import top.ysqorz.mybatis.session.Configuration;
import top.ysqorz.mybatis.session.SqlSession;

import java.util.Arrays;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 21:39
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration config;

    public DefaultSqlSession(Configuration config) {
        this.config = config;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object... args) {
        MappedStatement mappedStatement = config.getMappedStatement(statement);
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + Arrays.toString(args)
                + "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return config.getMapper(type, this);
    }

    public Configuration getConfig() {
        return config;
    }
}
