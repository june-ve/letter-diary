package com.juneve.letterdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LetterDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetterDiaryApplication.class, args);
    }

}
