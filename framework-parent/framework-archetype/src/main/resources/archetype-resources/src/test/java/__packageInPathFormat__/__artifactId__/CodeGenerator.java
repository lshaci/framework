package ${groupId}.${artifactId};

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Project code generator
 *
 * @author lshaci
 * @since 1.0.1
 */
public class CodeGenerator {

    /**
     * Project package
     */
    private static String projectPackage = "${groupId}.${artifactId}";

    /**
     * Database url
     */
    private static String url;
    /**
     * Database username
     */
    private static String username;
    /**
     * Database password
     */
    private static String password;
    /**
     * Database driver class
     */
    private static String driverClass;

    /**
     * Init database information
     */
    static {
        Properties properties = new Properties();
        InputStream i = CodeGenerator.class.getResourceAsStream("/generatorConfig.properties");
        try {
            properties.load(i);
            url = properties.getProperty("generator.jdbc.url");
            username = properties.getProperty("generator.jdbc.username");
            password = properties.getProperty("generator.jdbc.password");
            driverClass = properties.getProperty("generator.jdbc.driverClass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * main method, execute code generator
     */
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java"); // 输出文件目录
        gc.setFileOverride(true); // 是否覆盖已有文件
        gc.setOpen(false); // 是否打开输出目录
        gc.setAuthor("lshaci");
        gc.setSwagger2(true);  // 实体属性 Swagger2 注解
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
//        gc.setBaseResultMap(true); // mapper.xml中生成BaseResultMap
        gc.setActiveRecord(true);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        dsc.setUsername(username);
        dsc.setPassword(password);
        dsc.setDriverName(driverClass);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(projectPackage);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {

            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mappers/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }

        });
        cfg.setFileOutConfigList(focList);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setXml(null);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSkipView(true);
        strategy.setEntityLombokModel(true);
        strategy.setLogicDeleteFieldName("deleted");
        strategy.setVersionFieldName("version");

//        strategy.setInclude(); // 设置需要生成代码的表, 允许正则表达式(与exclude二选一配置)
        strategy.setExclude("no"); // 设置不需要代码生成的表
        strategy.setTablePrefix("t_");

        mpg.setGlobalConfig(gc);
        mpg.setDataSource(dsc);
        mpg.setPackageInfo(pc);
        mpg.setCfg(cfg);
        mpg.setTemplate(templateConfig);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }

}
