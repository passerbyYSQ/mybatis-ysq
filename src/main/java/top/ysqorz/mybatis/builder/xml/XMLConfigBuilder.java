package top.ysqorz.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import top.ysqorz.mybatis.builder.BaseBuilder;
import top.ysqorz.mybatis.mapping.MappedStatement;
import top.ysqorz.mybatis.mapping.SqlCommandType;
import top.ysqorz.mybatis.session.Configuration;
import top.ysqorz.mybatis.utils.ResourceUtils;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
            // 解析<mappers>标签
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return config;
    }

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
                Map<Integer, String> parameters = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 0; matcher.find(); i++) {
                    // String placeHolder = matcher.group(1); // 整个 #{} 占位符
                    String parameterName = matcher.group(2).trim(); // #{} 里面的变量名
                    parameters.put(i, parameterName);
                    // sql = sql.replace(placeHolder, "?"); // TODO 性能???
                }
                sql = sql.replaceAll(pattern.pattern(), "?");

                String statementId = namespace + "." + id;
                String nodeName = select.getName().toUpperCase(Locale.ENGLISH);
                // 暂时这里就是SELECT
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName);
                // TODO 为什么要将config传进去??
                MappedStatement mappedStatement = new MappedStatement.Builder(config, statementId,
                        sqlCommandType, parameterType, resultType, sql, parameters).build();
                config.addMappedStatement(mappedStatement);
            }

            config.addMapper(ResourceUtils.classForName(namespace));
        }
    }
}
