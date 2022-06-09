package top.ysqorz.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import top.ysqorz.mybatis.builder.BaseBuilder;
import top.ysqorz.mybatis.config.Configuration;
import top.ysqorz.mybatis.config.Environment;
import top.ysqorz.mybatis.datasource.DataSourceFactory;
import top.ysqorz.mybatis.mapping.BoundSql;
import top.ysqorz.mybatis.mapping.MappedStatement;
import top.ysqorz.mybatis.mapping.SqlCommandType;
import top.ysqorz.mybatis.transaction.TransactionFactory;
import top.ysqorz.mybatis.utils.ResourceUtils;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析mybatis-config.xml，目前只解析mapper.xml的路径
 * 解析xxxMapper.xml，目前只解析<select>
 *
 * @author passerbyYSQ
 * @create 2022-06-03 17:08
 */
public class XMLConfigBuilder extends BaseBuilder {
    // mybatis-config.xml
    private Element root;

    public XMLConfigBuilder(Reader reader) {
        super(new Configuration());
        // dom4j处理XML。TODO 后面可考虑换成jdk原生的API
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     */
    public Configuration parse() {
        try {
            // 解析<environment>标签，包括事务管理器和数据源工厂的配置信息
            environmentElement(root.element("environments"));

            // 解析<mappers>标签，包括Mapper.xml的路径信息
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return config;
    }

    /**
     * <environments default="development">
     *     <environment id="development">
     *         <transactionManager type="JDBC"/>
     *         <dataSource type="DRUID">
     *             <property name="driver" value="com.mysql.jdbc.Driver"/>
     *             <property name="url" value="jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true"/>
     *             <property name="username" value="root"/>
     *             <property name="password" value="123456"/>
     *         </dataSource>
     *     </environment>
     * </environments>
     */
    private void environmentElement(Element environments) throws InstantiationException, IllegalAccessException {
        String env = environments.attributeValue("default");
        List<Element> envList = environments.elements("environment");
        for (Element envElement : envList) {
            String envId = envElement.attributeValue("id");
            if (env.equals(envId)) {
                // 事务管理器
                String transManagerType = envElement.element("transactionManager").attributeValue("type");
                TransactionFactory transFactory = (TransactionFactory) config.getClassByAlias(transManagerType).newInstance();
                // 数据源工厂
                Element dataSourceElement = envElement.element("dataSource");
                String dataSourceType = dataSourceElement.attributeValue("type");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) config.getClassByAlias(dataSourceType).newInstance();
                // 数据源配置
                List<Element> propList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element propElement : propList) {
                    String name = propElement.attributeValue("name");
                    String value = propElement.attributeValue("value");
                    props.setProperty(name, value);
                }
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment environment = new Environment.Builder(envId)
                        .transactionFactory(transFactory)
                        .dataSource(dataSource)
                        .build();
                config.setEnvironment(environment);
            }
        }
    }

    /**
     * <mappers>
     *     <mapper resource="mapper/UserMapper.xml"/>
     * </mappers>
     */
    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element element : mapperList) {
            // Mapper.xml的路径
            String resource = element.attributeValue("resource");
            Reader mapperReader = ResourceUtils.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document mapperDocument = saxReader.read(new InputSource(mapperReader));
            Element root = mapperDocument.getRootElement();
            // 命名空间(Dao的全路径)
            String namespace = root.attributeValue("namespace");

            List<Element> selectElements = root.elements("select");
            for (Element select : selectElements) {
                String id = select.attributeValue("id");
                String parameterType = select.attributeValue("parameterType");
                String resultType = select.attributeValue("resultType");
                String sql = select.getText();

                // 将 #{} 替换成 ?
                Map<Integer, String> parameterMap = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 0; matcher.find(); i++) {
                    // String placeHolder = matcher.group(1); // 整个 #{} 占位符
                    String parameterName = matcher.group(2).trim(); // #{} 里面的变量名
                    parameterMap.put(i, parameterName);
                    // sql = sql.replace(placeHolder, "?"); // 性能问题
                }
                sql = sql.replaceAll(pattern.pattern(), "?");

                String statementId = namespace + "." + id;
                String nodeName = select.getName().toUpperCase(Locale.ENGLISH);
                // 暂时这里就是SELECT
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName);
                BoundSql boundSql = new BoundSql(sql, parameterMap, parameterType, resultType);
                // TODO 为什么要将config传进去??
                MappedStatement mappedStatement = new MappedStatement.Builder(config, statementId,
                        sqlCommandType, boundSql).build();
                config.addMappedStatement(mappedStatement);
            }

            config.addMapper(ResourceUtils.classForName(namespace));
        }
    }
}
