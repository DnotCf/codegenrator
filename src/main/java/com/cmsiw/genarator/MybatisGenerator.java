package com.cmsiw.genarator;



import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author tangs
 * @date 2019/9/3 10:40
 */

public class MybatisGenerator implements Runnable {


    public String url = "jdbc:mysql://localhost:3306/geo-hfan-test?characterEncoding=UTF-8&serverTimezone=GMT%2b8&allowMultiQueries=true";
    public String username = "root";
    public String password = "123456";
    public String driverName = "com.mysql.jdbc.Driver";
//    public String url = "jdbc:sqlserver://server.clzytech.com:8433;DatabaseName=geo_sc";
//    public String username = "geo_sc";
//    public String password = "geo_sc@123";
//    public String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    public GlobalConfig  configGlobal() {
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        gc.setEntityName("%s");
        gc.setIdType(IdType.ID_WORKER_STR);

        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");

        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("tangs");
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setBaseResultMap(true);

        return gc;
    }

    public DataSourceConfig  configDataSource(){
        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://47.108.155.74:3306/dev-cq?characterEncoding=UTF-8&serverTimezone=GMT%2b8&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true");
        // dsc.setSchemaName("public");
//        dsc.setDriverName("com.mysql.jdbc.Driver");
//        dsc.setUsername("root");
//        dsc.setPassword("117214");
        //dsc.setPassword("tangs1234");
        dsc.setUrl(url);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        /*dsc.setUrl("jdbc:oracle:thin:@192.168.0.69:1521:czkj");
        // dsc.setSchemaName("public");
        dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
        dsc.setUsername("czkjs");
        dsc.setPassword("czkjs");*/
        return dsc;
    }


    public PackageConfig  configPackage(){
        // 包配置
        PackageConfig pc = new PackageConfig();

        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.cmsiw");
        //pc.setEntity("pojo.entity");
        return pc;
    }

    public StrategyConfig configStrategy(PackageConfig pc) {

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("com.cmsiw.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setLogicDeleteFieldName("deleted");

        TableFill fill = new TableFill("operator", FieldFill.INSERT_UPDATE);
        TableFill fill1 = new TableFill("operate_time", FieldFill.INSERT_UPDATE);
        TableFill fill2 = new TableFill("operate_ip", FieldFill.INSERT_UPDATE);
        TableFill fill3 = new TableFill("create_time", FieldFill.INSERT);
        strategy.setTableFillList(Arrays.asList(fill,fill1,fill2,fill3));

        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setEntityBuilderModel(true);

        // 公共父类
        //strategy.setSuperControllerClass("com.cmsiw.common.BaseController");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");

        return strategy;
    }

    //模板配置
    public TemplateConfig templateConfig(){
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //templateConfig.setXml();
        return templateConfig;
    }

    //自定义配置
    public InjectionConfig customerConfig(PackageConfig pc) {
        String projectPath = System.getProperty("user.dir");
        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {


            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
//                String entityName = tableInfo.getEntityName();
//                entityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
                String entityName = scanner("实体参数名");
                entityName = StringUtils.isEmpty(entityName) == true ? "param" : entityName;
//                String entityName = "param"; //this.getConfig().getGlobalConfig().getEntityName();
                map.put("param",entityName);
                map.put("service", entityName+"Service");
                this.setMap(map);
            }

            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
//            @Override
//            public void initMap(TableInfo tableInfo) {
//                Map<String, Object> map = new HashMap<>();
//                String entityName = tableInfo.getEntityName();
//                entityName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
//                //String s = scanner("实体参数名");
//                //s = StringUtils.isEmpty(s) == true ? "param" : s;
//                //String name = this.getConfig().getGlobalConfig().getEntityName();
//                map.put("param",entityName);
//                map.put("service", entityName+"Service");
//                this.setMap(map);
//            }

            private String ups(String name) {
                if (null == name) {
                    return "param";
                }
                String substring = name.substring(0, 1);
                String substring1 = name.substring(1, name.length());
                return substring.toLowerCase() + substring1;
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        injectionConfig.setFileOutConfigList(focList);

        return injectionConfig;
    }

    @Override
    public void run(){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        mpg.setGlobalConfig(this.configGlobal());
        // 数据源配置
        mpg.setDataSource(configDataSource());
        // 包配置
        PackageConfig packageConfig = configPackage();
        mpg.setPackageInfo(packageConfig);

        //自定义配置
        mpg.setCfg(customerConfig(packageConfig));

        // 配置模板
        mpg.setTemplate(templateConfig());
        // 策略配置
        mpg.setStrategy(configStrategy(packageConfig));

        //模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */


    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        new MybatisGenerator().run();
    }


}
