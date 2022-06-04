package top.ysqorz.mybatis.builder;

import top.ysqorz.mybatis.session.Configuration;

/**
 * @author passerbyYSQ
 * @create 2022-06-03 17:07
 */
public class BaseBuilder {
    protected final Configuration config;

    public BaseBuilder(Configuration config) {
        this.config = config;
    }
}
