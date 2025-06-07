package com.wildrimak.credit_score.config;

import com.wildrimak.credit_score.infra.kafka.publisher.ConsultaKafkaPublisher;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestKafkaConfiguration {

    @Bean
    public ConsultaKafkaPublisher consultaKafkaPublisher() {
        return Mockito.mock(ConsultaKafkaPublisher.class);
    }

}
