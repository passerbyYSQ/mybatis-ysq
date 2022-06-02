package top.ysqorz.mybatis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ysqorz.mybatis.dao.IUserDao;

/**
 * @author passerbyYSQ
 * @create 2022-06-02 16:46
 */
public class IUserDaoImpl implements IUserDao {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String queryByUsername(String username) {
        log.info("executing queryByUsername ...");
        return username;
    }
}
