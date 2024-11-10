package com.birdbi.api;

import com.birdbi.api.constant.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(
        basePackages = {Constant.basePackage}
)
@EnableScheduling
@SpringBootApplication
public class BirdbiApplication extends SpringBootServletInitializer {
    public BirdbiApplication() {
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(new Class[]{BirdbiApplication.class});
    }

    public static void main(String[] args) {

        SpringApplication.run(BirdbiApplication.class, args);
    }
}