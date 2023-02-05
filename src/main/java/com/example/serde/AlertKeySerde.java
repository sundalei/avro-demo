package com.example.serde;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.example.model.Alert;

public class AlertKeySerde implements Serializer<Alert>, Deserializer<Alert> {

    @Override
    public Alert deserialize(String topic, byte[] value) {
        
        return null;
    }

    @Override
    public byte[] serialize(String topic, Alert key) {
        if (key == null) {
            return null;
        }

        return key.getStageId().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
    
}
