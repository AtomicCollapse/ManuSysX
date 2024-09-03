package top.hondaman.cloud.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "top.hondaman.cloud.infra")
@SpringBootApplication(scanBasePackages = {"top.hondaman.cloud.erp","top.hondaman.cloud.framework"})
public class ErpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpServerApplication.class,args);
    }
}
