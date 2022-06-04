package top.ysqorz.mybatis.binding;

import top.ysqorz.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 映射器(Mapper)代理类的生成工厂
 *
 * @author passerbyYSQ
 * @create 2022-06-02 17:02
 */
public class MapperProxyFactory<T> {
    // 代理类所需要实现的接口
    private final Class<T> mapperInterface;
    // 接口方法的缓存
    private Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * 创建被代理类的实例对象
     */
    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        // 如果创建了多个实例，也会共用一个methodCache
        MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
