package com.example.hhblogdevelop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HhblogDevelopApplication {

    public static void main(String[] args) {
        SpringApplication.run(HhblogDevelopApplication.class, args);
    }

}
