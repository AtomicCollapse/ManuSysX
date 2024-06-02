package top.hondaman.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.hondaman.cloud.gateway","top.hondaman.cloud.framework"})
public class GatewayServerApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(GatewayServerApplication.class, args);
    }
}
