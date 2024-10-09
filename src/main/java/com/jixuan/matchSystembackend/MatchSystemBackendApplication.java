package com.jixuan.matchSystembackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jixuan.matchSystembackend.mapper")
public class MatchSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchSystemBackendApplication.class, args);
    }

}
