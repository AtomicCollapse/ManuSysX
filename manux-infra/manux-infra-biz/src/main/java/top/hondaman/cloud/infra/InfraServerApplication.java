package top.hondaman.cloud.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.hondaman.cloud.infra","top.hondaman.cloud.framework","top.hondaman.cloud.system"})
public class InfraServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfraServerApplication.class,args);
    }
}
