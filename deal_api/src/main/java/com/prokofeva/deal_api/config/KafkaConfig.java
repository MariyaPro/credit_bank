package com.prokofeva.deal_api.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic finishRegistration(){
        return new NewTopic("finish-registration",1,(short) 1);
    }
    @Bean
    public NewTopic createDocuments(){
        return new NewTopic("create-documents",1,(short) 1);
    }
    @Bean
    public NewTopic sendDocuments(){
        return new NewTopic("send-documents",1,(short) 1);
    }
    @Bean
    public NewTopic sendSes(){
        return new NewTopic("send-ses",1,(short) 1);
    }
    @Bean
    public NewTopic creditIssued(){
        return new NewTopic("credit-issued",1,(short) 1);
    }
    @Bean
    public NewTopic statementDenied(){
        return new NewTopic("statement-denied",1,(short) 1);
    }

}