package org.spring1.realwordjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "org.spring1.realwordjob")
public class RealWordJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealWordJobApplication.class, args);
    }
}
