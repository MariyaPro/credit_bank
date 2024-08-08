package com.prokofeva.deal_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableKafka
public class DealApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealApiApplication.class, args);
    }

}