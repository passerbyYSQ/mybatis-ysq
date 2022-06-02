package top.ysqorz.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 映射器代理类的生成工厂
 *
 * @author passerbyYSQ
 * @create 2022-06-02 17:02
 */
public class MapperProxyFactory<T> {
    // 代理类所需要实现的接口
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * 创建被代理类的实例对象
     */
    public T newInstance(Map<String, String> sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
