package top.ysqorz.mybatis.dao.impl;

import com.alibaba.fastjson.JSON;
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
    public String queryUserById(String userId) {
        log.info("executing queryUserById ...");
        User user = new User();
        user.setId(1L);
        user.setUserId(userId);
        user.setUsername("zhangsan");
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        return JSON.toJSONString(user);
    }
}
