package top.hondaman.cloud.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.hondaman.cloud"})
public class InfraServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfraServerApplication.class,args);
    }
}
