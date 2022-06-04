package top.ysqorz.mybatis.mapping;

import top.ysqorz.mybatis.session.Configuration;

import java.util.Map;

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
    private String parameterType;
    private String resultType;
    private String sql;
    private Map<Integer, String> parameters;

    /**
     * constructor disabled
     */
    MappedStatement() {

    }

    /**
     * 禁用构造方法，通过建造者构建MappedStatement实例
     */
    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration config, String id, SqlCommandType sqlCommandType,
                       String parameterType, String resultType, String sql, Map<Integer, String> parameters) {
            mappedStatement.config = config;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameters = parameters;
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

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, String> parameters) {
        this.parameters = parameters;
    }
}
