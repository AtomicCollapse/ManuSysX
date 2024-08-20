package top.hondaman.cloud.infra.framework.codegen;

import lombok.Data;

@Data
public class Student {
    private String name;
    private String sex;
    private Double score;
    public Student(String name,String sex,Double score){
        this.name = name;
        this.sex = sex;
        this.score = score;
    }
}
