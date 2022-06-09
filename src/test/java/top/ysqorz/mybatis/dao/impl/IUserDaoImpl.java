package top.ysqorz.mybatis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.dao.IUserDao;
import top.ysqorz.mybatis.po.User;

import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 16:46
 */
public class IUserDaoImpl implements IUserDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public User queryUserById(Long id) {
        log.info("executing queryUserById ...");
        User user = new User();
        user.setId(id);
        user.setUserId("user123");
        user.setUsername("zhangsan");
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        return user;
    }
}
