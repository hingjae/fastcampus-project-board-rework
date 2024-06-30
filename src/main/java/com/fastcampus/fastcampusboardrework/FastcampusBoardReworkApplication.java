package com.fastcampus.fastcampusboardrework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class FastcampusBoardReworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastcampusBoardReworkApplication.class, args);
    }

}
