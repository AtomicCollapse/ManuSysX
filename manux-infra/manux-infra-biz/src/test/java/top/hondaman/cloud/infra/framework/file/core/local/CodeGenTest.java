package top.hondaman.cloud.infra.framework.file.core.local;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import lombok.Data;
import org.junit.jupiter.api.Test;
import top.hondaman.cloud.infra.framework.codegen.Student;
import java.io.File;
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



}