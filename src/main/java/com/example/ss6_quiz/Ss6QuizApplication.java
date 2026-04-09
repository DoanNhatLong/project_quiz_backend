package com.example.ss6_quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class Ss6QuizApplication {

    public static void main(String[] args) {
        System.out.println(">>> CHECKPOINT: PHIEN BAN 2 <<<");
        SpringApplication.run(Ss6QuizApplication.class, args);
    }

}
