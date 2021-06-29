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
package io.github.ognis1205.tweet_visualization.storm;

import java.util.Map;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
public class ProcessorTopology {
    public static class ExclamationBolt extends BaseRichBolt {
        OutputCollector collector;

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
            this.collector = collector;
        }

        @Override
        public void execute(Tuple tuple) {
            String word = tuple.getString(0);
            StringBuilder exclamatedWord = new StringBuilder();
            exclamatedWord.append(word).append("!!!");
            this.collector.emit(tuple, new Values(exclamatedWord.toString()));
            this.collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("exclamated-word"));
        }
    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option kafkaServers = Option.builder("k")
                .required(true)
                .desc("specifies Kafka bootstrap servers in csv format")
                .longOpt("kafka-bootstrap-servers")
                .build();
        Option kafkaTopic = Option.builder("t")
                .required(true)
                .desc("specifies Kafka topic")
                .longOpt("kafka-topic")
                .build();
        options.addOption(kafkaServers);
        options.addOption(kafkaTopic);

        Properties props = new Properties();
        CommandLine commandLine;
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            props.put("bootstrap.servers", commandLine.getOptionValue("k"));
            props.put("topic", commandLine.getOptionValue("t"));
        } catch (ParseException exception) {
            System.out.print("Parse error: ");
            System.out.println(exception.getMessage());
        }
        props.put("acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig
                .builder(props.getProperty("bootstrap.servers"), props.getProperty("topic"))
                .setProp(props)
                .setRecordTranslator((r) -> new Values(r.topic(), r.key(), r.value()), new Fields("topic", "key", "message"))
                .build();
        KafkaSpout<String, String> kafkaSpout = new KafkaSpout<>(kafkaSpoutConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka", kafkaSpout, 10);
        builder.setBolt("exclaim1", new ExclamationBolt(), 3).shuffleGrouping("kafka");
        builder.setBolt("exclaim2", new ExclamationBolt(), 2).shuffleGrouping("exclaim1");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(3);

        StormSubmitter.submitTopology("exclamation-topology", conf, builder.createTopology());
    }
}
