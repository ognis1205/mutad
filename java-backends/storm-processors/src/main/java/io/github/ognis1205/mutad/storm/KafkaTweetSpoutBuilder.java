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
package io.github.ognis1205.mutad.storm;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.kafka.spout.FirstPollOffsetStrategy;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class KafkaTweetSpoutBuilder {
    /** Field name. */
    public static final String FIELD = "json";

    /**
     * Instanciate `KafkaSpout` instance.
     * @param bootstrapServers Comma-separated Kafka bootstrap servers.
     * @param topic Kafka topic.
     * @param groupId Kafka consumer group id.
     */
    public static KafkaSpout<String, String> build(String bootstrapServers, String topic, String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,                 groupId                           );
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.SECURITY_PROVIDERS_CONFIG,       StringDeserializer.class.getName());

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig
                .builder(bootstrapServers, topic)
                .setProp(props)
                .setFirstPollOffsetStrategy(FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST)
                .setRecordTranslator((r) -> new Values(r.value()), new Fields(FIELD))
                .build();

        return new KafkaSpout<String, String>(kafkaSpoutConfig);
    }

    private KafkaTweetSpoutBuilder() {}
}
