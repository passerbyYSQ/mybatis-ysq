package top.ysqorz.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import top.ysqorz.mybatis.config.Configuration;
import top.ysqorz.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射器(Mapper)工厂的注册中心，管理所有注册的Mapper
 *
 * @author passerbyYSQ
 * @create 2022-06-02 21:05
 */
public class MapperRegistry {
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();
    private Configuration config;

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> void addMapper(Class<T> type) {
        if (!type.isInterface()) {
            return;
        }
        if (hasMapper(type)) {
            throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
        }
        knownMappers.put(type, new MapperProxyFactory<>(type));
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        // TODO 后面不使用Hutool
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }
}
