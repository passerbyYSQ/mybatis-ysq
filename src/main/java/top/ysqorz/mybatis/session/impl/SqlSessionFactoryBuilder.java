package top.ysqorz.mybatis.session.impl;

import top.ysqorz.mybatis.builder.xml.XMLConfigBuilder;
import top.ysqorz.mybatis.session.Configuration;
import top.ysqorz.mybatis.session.SqlSessionFactory;

import java.io.Reader;

/**
 * 由于SqlSessionFactory的创建需要读取和解析配置文件等操作，
 * 所以需要专门的建造者来负责
 *
 * @author passerbyYSQ
 * @create 2022-06-03 17:04
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    private SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

}
