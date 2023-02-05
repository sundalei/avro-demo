package com.example;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditProducer.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Properties kaProperties = new Properties();
        
        // kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kaProperties.put("bootstrap.servers", "localhost:29092");
        kaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kaProperties.put("acks", "all");
        kaProperties.put("retries", "3");
        kaProperties.put("max.in.flight.requests.per.connection", "1");

        try (Producer<String, String> producer = new KafkaProducer<>(kaProperties)) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String,String>("kinaction_audit", null, "audit event");

            RecordMetadata result = producer.send(producerRecord).get();
            LOGGER.info("kinaction_info offset = {}, topic = {}, timestamp = {}", result.offset(), result.topic(), result.timestamp());
        }
    }
}
