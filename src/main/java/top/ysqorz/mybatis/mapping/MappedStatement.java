package top.ysqorz.mybatis.mapping;

import top.ysqorz.mybatis.config.Configuration;

/**
 * Mapper.xml中的SQL语句标签解析之后所对应的实体类
 *
 * @author passerbyYSQ
 * @create 2022-06-03 17:19
 */
public class MappedStatement {
    private Configuration config;
    private String id; // TODO 如何标识一个SQL语句标签???
    private SqlCommandType sqlCommandType;
    private BoundSql boundSql;

    /**
     * constructor disabled
     */
    private MappedStatement() {

    }

    /**
     * 禁用构造方法，通过建造者构建MappedStatement实例
     */
    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration config, String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.config = config;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert mappedStatement.config != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public void setBoundSql(BoundSql boundSql) {
        this.boundSql = boundSql;
    }
}
