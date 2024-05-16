package top.hondaman.cloud.module.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"top.hondaman.cloud"})
public class PmsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PmsServerApplication.class,args);
    }
}
