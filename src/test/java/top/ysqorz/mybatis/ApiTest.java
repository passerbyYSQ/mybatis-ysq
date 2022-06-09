package top.ysqorz.mybatis;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.dao.IUserDao;
import top.ysqorz.mybatis.dao.impl.IUserDaoImpl;
import top.ysqorz.mybatis.po.User;
import top.ysqorz.mybatis.session.SqlSession;
import top.ysqorz.mybatis.session.SqlSessionFactory;
import top.ysqorz.mybatis.session.impl.SqlSessionFactoryBuilder;
import top.ysqorz.mybatis.utils.ResourceUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 16:26
 */
public class ApiTest {
    private static final Logger log = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void testMapperProxyFactory() throws IOException {
        // 读取mybatis-config.xml
        Reader configReader = ResourceUtils.getResourceAsReader("mybatis-config.xml");
        // 创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configReader);
        // 获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 获取代理后的Dao
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        // 测试
        User user = userDao.queryUserById(1L);
        log.info("测试结果：{}", JSON.toJSONString(user));
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
        log.info("动态代理后：{}", userDao.queryUserById(1L));
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
