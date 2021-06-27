/*
 * Copyright 2021 Shingo OKAWA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ognis1205.tweet_visualization.data_collectors.impl;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import lombok.extern.slf4j.Slf4j;
import io.github.ognis1205.tweet_visualization.data_collectors.Sink;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
public class KafkaSink implements Sink<String> {
    /** Kafka brokers' names. */
    private String brokers;

    /** Kafka topic. */
    private String topic;

    /** Kafka producer/client. */
    private KafkaProducer<String, String> producer;

    /**
     * Initialize `KafkaSink` instance.
     * @param topic Kafka topic.
     * @param brokers Kafka broker.
     */
    public KafkaSink(String brokers, String topic) {
        this.brokers = brokers;
        this.topic = topic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {
        log.info("starting up producer to sink tweets to Kafka...");
        // Create Producer properties.
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Create safe Producer.
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        // High throughput producer.
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        // Create Producer.
        this.producer = new KafkaProducer<String, String>(properties);
        log.info("starting up producer to sink tweets to Kafka: done!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String data) {
        if (data != null) {
            producer.send(new ProducerRecord<>(topic,null, data), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        log.error("something bad happened", e);
                    }
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        log.info("shutting down producer...");
        this.producer.close();
        log.info("shutting down producer: done!");
    }
}
