package top.ysqorz.mybatis.session.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.config.Configuration;
import top.ysqorz.mybatis.mapping.BoundSql;
import top.ysqorz.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 21:39
 */
public class DefaultSqlSession implements SqlSession {
    private static final Logger log = LoggerFactory.getLogger(DefaultSqlSession.class);
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
        try {
            // 获取数据库连接
            Connection connection = config.getEnvironment().getDataSource().getConnection();
            // 获取要执行的SQL
            BoundSql boundSql = config.getMappedStatement(statement).getBoundSql();
            // 预编译SQL
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            // TODO 参数设置先写死，先将流程打通
            preparedStatement.setLong(1, Long.parseLong(args[0].toString()));
            log.info("Prepared SQL: {}", preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<T> list = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
            return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO 目前简单地将ResultSet转换成POJO
     */
    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        // 遍历每一行
        while (resultSet.next()) {
            T obj = (T) clazz.newInstance();
            // 遍历当前行的每一列
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                String columnName = metaData.getColumnName(i);
                // 默认属性名和字段名一样
                String setMethod = "set" + Character.toUpperCase(columnName.charAt(0)) + columnName.substring(1);
                Method method;
                if (value instanceof Timestamp) {
                    method = clazz.getMethod(setMethod, LocalDateTime.class);
                    value = ((Timestamp) value).toLocalDateTime();
                } else {
                    method = clazz.getMethod(setMethod, value.getClass());
                }
                method.invoke(obj, value);
            }
            list.add(obj);
        }
        return list;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return config.getMapper(type, this);
    }

    public Configuration getConfig() {
        return config;
    }
}
