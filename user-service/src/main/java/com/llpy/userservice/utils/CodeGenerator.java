package com.llpy.userservice.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author TT
 * @Date 2021-03-22
 * @return 代码生成器1.0
 */

public class CodeGenerator {

    public static void main(String[] args) {
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);

        mpg.setGlobalConfig(new GlobalConfig()
                .setOutputDir(projectPath + "/clientServer/src/main/java")
                // 是否覆盖
                .setFileOverride(true)
                // 开启AR模式
                .setActiveRecord(true)
                // XML二级缓存
                .setEnableCache(false)
                // 生成ResultMap
                .setBaseResultMap(true)
                // 生成 sql片段
                .setBaseColumnList(true)
                // 自动打开生成后的文件夹
                .setOpen(false)
                // 自定义文件命名,%s会自动填充表实体类名字
                .setMapperName("%sMapper")
                .setXmlName("%sMapper")
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
                .setControllerName("%sController")
                .setSwagger2(true)
                .setAuthor("LLPY")
                .setIdType(IdType.ID_WORKER)
                .setDateType(DateType.ONLY_DATE)
        );
        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.71:8711/first_client?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("hj134679");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);
        // 4、包配置
        mpg.setPackageInfo(new PackageConfig()
                        // 基本包路径
                        .setParent("com.hj.accounting")
                        // 设置Controller包名
                        .setController("controller")
                        // 设置entity包名
                        .setEntity("entity")
                        // 设置Mapper包名
                        .setMapper("mapper")
                        // 设置Service包名
                        .setService("service")
                        // 设置Service实现类包名
                        .setServiceImpl("service.impl")
                // 设置Mapper.xml包名

        );
        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("ca_carry_forward_production_costs_set");
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
//        strategy.setTablePrefix(pc.getModuleName() + "_"); //生成实体时去掉表前缀
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段 映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.setRestControllerStyle(true);
        //restful api风格控制器
//        strategy.setTablePrefix("t_");
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符
        mpg.setStrategy(strategy);
        mpg.setCfg(new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(1);
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        }.setFileOutConfigList(Collections.singletonList(
                new FileOutConfig("/templates/mapper.xml.vm") {
                    // 自定义Mapper.xml输出路径
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return "E:\\OnlineAccounting\\clientServer\\src\\main\\resources\\xml/" + tableInfo.getEntityName() + "Mapper.xml";
                    }
                })));

        // 6、执行
        mpg.execute();
        System.out.println("------------------------执行完毕!----------------------------");
    }

}

