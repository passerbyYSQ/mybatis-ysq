package top.ysqorz.mybatis.config;

import top.ysqorz.mybatis.datasource.druid.DruidDataSourceFactory;
import top.ysqorz.mybatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 类型别名注册中心
 *
 * @author passerbyYSQ
 * @create 2022-06-06 19:17
 */
public class TypeAliasRegistry {

    private final Map<String, Class<?>> typeAliases = new HashMap<>();

    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        // 基本包装类型
        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);

        registerAlias("JDBC", JdbcTransactionFactory.class);
        registerAlias("DRUID", DruidDataSourceFactory.class);
    }

    public void registerAlias(String alias, Class<?> value) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        typeAliases.put(key, value); // TODO 是否考虑重复??
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> resolveAlias(String alias) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        return (Class<T>) typeAliases.get(key);
    }
}
