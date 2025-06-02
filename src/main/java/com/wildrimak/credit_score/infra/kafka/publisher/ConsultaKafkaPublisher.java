package com.wildrimak.credit_score.infra.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildrimak.credit_score.infra.kafka.dtos.ConsultaLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class ConsultaKafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(ConsultaKafkaPublisher.class);

    @Value("${messaging.topics.consultas}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ConsultaKafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publish(ConsultaLog log) {
        toJson(log).ifPresent(json -> kafkaTemplate.send(topic, json));
    }

    private Optional<String> toJson(ConsultaLog consultaLog) {
        try {
            return Optional.of(objectMapper.writeValueAsString(consultaLog));
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter ConsultaLog para JSON", e);
            return Optional.empty();
        }
    }

}
