package top.ysqorz.mybatis.binding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.mapping.MappedStatement;
import top.ysqorz.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射器(Mapper)代理类，描述代理时做哪些增强操作
 *
 * @author passerbyYSQ
 * @create 2022-06-02 17:05
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MapperProxy.class);

    private SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object中的方法不进行代理
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args); // TODO this ???
        }
        log.info("method name: {}", method.getName());
        final MapperMethod mapperMethod = cacheMapperMethod(method);
        return mapperMethod.execute(sqlSession, args);
    }

    private MapperMethod cacheMapperMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null) {
            String statementId = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = sqlSession.getConfig().getMappedStatement(statementId);
            mapperMethod = new MapperMethod(method, mappedStatement);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
