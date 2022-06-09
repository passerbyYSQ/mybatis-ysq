package top.ysqorz.mybatis.dao;

import top.ysqorz.mybatis.po.User;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 16:26
 */
public interface IUserDao {
    User queryUserById(Long id);
}
