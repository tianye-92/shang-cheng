package org.sl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.sl.dao")
public class ShangChengApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShangChengApplication.class, args);
    }

}
