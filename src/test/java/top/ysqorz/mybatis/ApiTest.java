package top.ysqorz.mybatis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.binding.MapperProxyFactory;
import top.ysqorz.mybatis.binding.MapperRegistry;
import top.ysqorz.mybatis.dao.IUserDao;
import top.ysqorz.mybatis.dao.impl.IUserDaoImpl;
import top.ysqorz.mybatis.session.SqlSession;
import top.ysqorz.mybatis.session.impl.DefaultSqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 16:26
 */
public class ApiTest {
    private static final Logger log = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void testMapperProxyFactory() {
        // 注册Mapper
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("top.ysqorz.mybatis.dao");

        // 获取SqlSession
        DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 获取代理后的Mapper
        // SqlSession中持有MapperRegistry，在获取出Mapper的时候，会将当前的SqlSession给Mapper持有
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 测试结果
        String res = userDao.queryByUsername("zhangsan");
        log.info("测试结果：{}", res);
    }

    /**
     * 回顾JDK动态代理
     */
    @Test
    public void testProxyClass() {
        // JDK动态代理，通过反射动态生成代理类和它的实例
        IUserDaoImpl userDaoImpl = new IUserDaoImpl();
        MyHandler myHandler = new MyHandler(userDaoImpl);
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(), // 采用当前线程的类加载器
                userDaoImpl.getClass().getInterfaces(), // 被代理类所实现的接口
                myHandler
        );
        log.info("动态代理后：{}", userDao.queryByUsername("ysq"));
    }

    static class MyHandler implements InvocationHandler {
        // 被代理的接口的实现类对象
        private Object target;

        public MyHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            log.info("====== {} before ======", method.getName());
            // 通过反射调用被代理对象的方法
            Object res = method.invoke(target, args);
            log.info("====== {} after ======", method.getName());
            return res;
        }
    }
}
