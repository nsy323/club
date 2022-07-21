package com.nsy.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  //BaseEntity를 사용하기 위해 추가
public class ClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubApplication.class, args);
    }

}
