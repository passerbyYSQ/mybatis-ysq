package top.ysqorz.mybatis.binding;

import top.ysqorz.mybatis.mapping.MappedStatement;
import top.ysqorz.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @author passerbyYSQ
 * @create 2022-06-04 16:06
 */
public class MapperMethod {
    private Method method;
    private MappedStatement mappedStatement;

    public MapperMethod(Method method, MappedStatement mappedStatement) {
        this.method = method;
        this.mappedStatement = mappedStatement;
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (mappedStatement.getSqlCommandType()) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(mappedStatement.getId(), args);
                break;
            case UNKNOWN:
                throw new RuntimeException("Unknown execution method for: " + mappedStatement.getId());
        }
        return result;
    }
}
