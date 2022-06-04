package top.ysqorz.mybatis.session;

/**
 * SQL会话工厂
 *
 * @author passerbyYSQ
 * @create 2022-06-02 21:12
 */
public interface SqlSessionFactory {
    /**
     * 打开一个SQL会话
     */
    SqlSession openSession();
}
