package top.ysqorz.mybatis.mapping;

import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2022-06-07 17:15
 */
public class BoundSql {
    private String sql;
    private Map<Integer, String> parameterMap;
    private String parameterType;
    private String resultType;

    public BoundSql(String sql, Map<Integer, String> parameterMap, String parameterType, String resultType) {
        this.sql = sql;
        this.parameterMap = parameterMap;
        this.parameterType = parameterType;
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<Integer, String> parameterMap) {
        this.parameterMap = parameterMap;
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
}
