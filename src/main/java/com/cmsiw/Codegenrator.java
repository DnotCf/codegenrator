package com.cmsiw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author tangs
 * @date 2019/9/3 11:21
 */
@SpringBootApplication(scanBasePackages = "com.cmsiw",exclude = {
//        SecurityAutoConfiguration.class
})
@MapperScan("com.cmsiw.sys.mapper")

public class Codegenrator {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public static void main(String[] args) {
        SpringApplication.run(Codegenrator.class, args);
    }
}
