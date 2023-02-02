package com.example;

import java.time.Instant;
import java.util.Properties;

import com.example.avro.Alert;
import com.example.avro.AlertStatus;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldProducer {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldProducer.class);

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092");
        kaProperties.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        kaProperties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        kaProperties.put("schema.registry.url", "http://localhost:8081");

        try (Producer<Long, Alert> producer = new KafkaProducer<>(kaProperties)) {
            Alert alert = new Alert(12345L, Instant.now().toEpochMilli(), AlertStatus.Critical);

            LOG.info("kinaction_info Alert -> {}", alert);

            ProducerRecord<Long, Alert> producerRecord =
                    new ProducerRecord<>("kinaction_schematest", alert.getSensorId(), alert);

            producer.send(producerRecord);
        }
    }
}