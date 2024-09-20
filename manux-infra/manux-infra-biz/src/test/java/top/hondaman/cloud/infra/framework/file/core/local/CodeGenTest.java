package top.hondaman.cloud.infra.framework.file.core.local;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import lombok.Data;
import org.junit.jupiter.api.Test;
import top.hondaman.cloud.infra.framework.codegen.Student;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenTest {


    /**
     * 测试Thymeleaf模板代码生成
     */
    @Test
    public void codeGenTest(){
        // 初始化 TemplateEngine 属性
        TemplateConfig config = new TemplateConfig("codegen",TemplateConfig.ResourceMode.CLASSPATH);
        TemplateEngine templateEngine = new VelocityEngine(config);
        //模板路径
        String vmPath = "java/controller/controller.vm";
        //输出保存路径
        String toPath = "C://codegen/codegen111.txt";
        //数据源
        Map<String, Object> bindingMap = initBindingMap();
        List<Student> columns = new ArrayList<>();
        columns.add(new Student("张三","男",67.98));
        columns.add(new Student("李四","男",99.99));
        columns.add(new Student("王五","女",60.18));
        bindingMap.put("list",columns);

        bindingMap.put("student1",new Student("赵6","女",88.88));
        Template template = templateEngine.getTemplate(vmPath);
        template.render(bindingMap,new File(toPath));
    }

    private Map initBindingMap(){
        Map<String, Object> result = new HashMap<>();
        return result;
    }


    /**
     * 测试JDBC获取数据库表结构
     */
    @Test
    public void getDataBaseStruct() throws Exception{
        String JDBCUrl = "jdbc:postgresql://127.0.0.1:5432/manux";
        //获取jdbc连接
        Connection connection = DriverManager.getConnection(JDBCUrl,"postgres","12345678");
        //从connection中获取数据库的元数据
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        System.out.println(databaseMetaData.getCatalogs());//连接的库名
        System.out.println(databaseMetaData.getDatabaseProductName());//数据库类型
        System.out.println(databaseMetaData.getDatabaseProductVersion());//数据库版本号

        //获取库中所有的表
        ResultSet resultSet = databaseMetaData.getTables(null,"infra","%",null);

        while(resultSet.next()){
            String tableType = resultSet.getString("TABLE_TYPE");//表类型
            if(StrUtil.isNotEmpty(tableType) && tableType.equals("TABLE")){
                String tableName = resultSet.getString("TABLE_NAME");//表名
                String tableRemark = resultSet.getString("REMARKS");//表备注
                System.out.println("/*--------------*/");
                System.out.println(String.format("%s %s",tableName,tableRemark));
                System.out.println("/*--------------*/");

                //表字段信息
                ResultSet columnsRs = databaseMetaData.getColumns(null,"infra",tableName,null);
                while (columnsRs.next()){
                    System.out.println(
                            columnsRs.getString("ORDINAL_POSITION")//字段序号
                            +" "+columnsRs.getString("column_name")//字段名
                            +" "+columnsRs.getString("TYPE_NAME")//字段类型
                            +" "+columnsRs.getString("COLUMN_SIZE")//长度
                            +" "+columnsRs.getString("DECIMAL_DIGITS")//精度
                            +" "+columnsRs.getString("COLUMN_DEF")//默认值
                            +" "+columnsRs.getString("REMARKS")//注释
                            );
                }
            }
        }

    }
}