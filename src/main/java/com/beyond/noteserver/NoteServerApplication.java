package com.beyond.noteserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NoteServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteServerApplication.class, args);
    }

}
