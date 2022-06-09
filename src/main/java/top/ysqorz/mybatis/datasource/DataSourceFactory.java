package top.ysqorz.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author passerbyYSQ
 * @create 2022-06-07 14:26
 */
public interface DataSourceFactory {

    /**
     * 数据源配置
     */
    void setProperties(Properties props);

    /**
     * 获取数据源
     */
    DataSource getDataSource();
}
