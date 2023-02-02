package com.example;

import com.example.avro.Alert;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class HelloWorldConsumer {

    private final static Logger LOG = LoggerFactory.getLogger(HelloWorldConsumer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092");
        kaProperties.put("group.id", "kinaction_helloconsumer");
        kaProperties.put("enable.auto.commit", "true");
        kaProperties.put("auto.commit.interval.ms", "1000");
        kaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        kaProperties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        kaProperties.put("schema.registry.url", "http://localhost:8081");

        HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
        helloWorldConsumer.consume(kaProperties);

        Runtime.getRuntime().addShutdownHook(new Thread(helloWorldConsumer::shutdown));
    }

    private void consume(Properties kaProperties) {
        try (KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(kaProperties)) {
            consumer.subscribe(List.of("kinaction_schematest"));

            while (keepConsuming) {
                ConsumerRecords<Long, Alert> consumerRecords = consumer.poll(Duration.ofMillis(250));
                for (ConsumerRecord<Long, Alert> record : consumerRecords) {
                    LOG.info("kinaction_info offset = {}, kinaction_value = {}", record.offset(), record.value());
                }
            }
        }
    }

    private void shutdown() {
        keepConsuming = false;
    }
}
