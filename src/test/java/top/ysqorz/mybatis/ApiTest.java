package top.ysqorz.mybatis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.binding.MapperProxyFactory;
import top.ysqorz.mybatis.dao.IUserDao;
import top.ysqorz.mybatis.dao.impl.IUserDaoImpl;

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
        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);

        // 模拟构建sqlSession
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("top.ysqorz.mybatis.dao.IUserDao.queryByUsername", "模拟执行 Mapper.xml 中的SQL语句");

        IUserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        log.info("动态代理后：{}", userDao.queryByUsername("ysq"));
    }

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
