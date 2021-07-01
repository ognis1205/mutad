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
package io.github.ognis1205.tweet_visualization.storm.sinks;

import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class KafkaTweetSink extends BaseRichBolt {
    /** SL4J Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(KafkaTweetSink.class);

    /** `OutputCollector` instance to expose the API for emitting tuples. */
    OutputCollector collector;

    /** Kafka brokers in comma-separated format. */
    private String brokerList;

    /** Kafka topic. */
    private String topic;

    /** Kafka producer/client. */
    private KafkaProducer<String, String> producer;

    /**
     * Initialize `TweetCollector` instance.
     * @param brokerList Kafka broker list in comma-separated format.
     * @param topic Kafka topic.
     */
    public KafkaTweetSink(String brokerList, String topic) {
        this.brokerList = brokerList;
        this.topic = topic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        this.collector = collector;
        LOG.info("starting up producer to sink tweets to Kafka...");
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,              this.brokerList                    );
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,           StringSerializer.class.getName()   );
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,         StringSerializer.class.getName()   );
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,             "true"                             );
        properties.setProperty(ProducerConfig.ACKS_CONFIG,                           "all"                              );
        properties.setProperty(ProducerConfig.RETRIES_CONFIG,                        Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5"                                );
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,               "snappy"                           );
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,                      "20"                               );
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,                     Integer.toString(32 * 1024)     );
        this.producer = new KafkaProducer<String, String>(properties);
        LOG.info("starting up producer to sink tweets to Kafka: done!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Tuple tuple) {
        String tweet = tuple.getString(0);
        LOG.trace(tweet);
        this.producer.send(new ProducerRecord<String, String>(this.topic,null, tweet), new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    LOG.error("something bad happened", e);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // Sink does not emit any tuples.
    }
}