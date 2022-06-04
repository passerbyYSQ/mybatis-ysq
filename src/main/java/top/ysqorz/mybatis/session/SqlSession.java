package top.ysqorz.mybatis.session;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 21:11
 */
public interface SqlSession {

    <T> T selectOne(String statement);

    /**
     * 根据SQL ID获取一条记录的封装对象
     *
     * @param statement SQL ID
     * @param args      SQL中的参数
     * @return          封装后的对象(PO)
     * @param <T>       封装后的对象类型
     */
    <T> T selectOne(String statement, Object... args);

    /**
     * 获取跟当前SqlSession绑定的Mapper
     *
     * @param type
     * @return      Mapper
     * @param <T>   Mapper的类型
     */
    <T> T getMapper(Class<T> type);

    Configuration getConfig();
}
