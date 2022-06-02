package top.ysqorz.mybatis.binding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射器代理类
 *
 * @author passerbyYSQ
 * @create 2022-06-02 17:05
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {
    private static final Long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MapperProxy.class);

    private Map<String, String> sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object中的方法不进行代理
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args); // this ???
        }
        log.info("method name: {}", method.getName());
        return "你被代理了！" + sqlSession.get(mapperInterface.getName() + "." + method.getName());
    }
}
