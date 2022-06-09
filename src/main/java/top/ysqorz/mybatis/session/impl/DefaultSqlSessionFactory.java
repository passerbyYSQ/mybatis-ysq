package top.ysqorz.mybatis.session.impl;

import top.ysqorz.mybatis.config.Configuration;
import top.ysqorz.mybatis.session.SqlSession;
import top.ysqorz.mybatis.session.SqlSessionFactory;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 21:40
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration config;

    public DefaultSqlSessionFactory(Configuration config) {
        this.config = config;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(config);
    }
}
