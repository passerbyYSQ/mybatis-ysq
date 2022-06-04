package top.ysqorz.mybatis.session;

import top.ysqorz.mybatis.binding.MapperRegistry;
import top.ysqorz.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有配置。存储所有的Mapper代理对象和解析后的SQL语句
 *
 * @author passerbyYSQ
 * @create 2022-06-03 17:08
 */
public class Configuration {

    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }
}
