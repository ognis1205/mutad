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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import io.github.ognis1205.tweet_visualization.storm.bolts.TweetCleanBolt;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class ProcessorTopology {
    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option kafkaServers = Option.builder("k")
                .required(true)
                .hasArg(true)
                .desc("specifies Kafka bootstrap servers in csv format")
                .longOpt("kafka-bootstrap-servers")
                .build();

        Option kafkaTopic = Option.builder("t")
                .required(true)
                .hasArg(true)
                .desc("specifies Kafka topic")
                .longOpt("kafka-topic")
                .build();

        options.addOption(kafkaServers);
        options.addOption(kafkaTopic);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, args);
            TopologyBuilder builder = new TopologyBuilder();

//            builder.setSpout(
//                    "kafka",
                    //KafkaTweetSpoutBuilder.build(commandLine.getOptionValue("k"), commandLine.getOptionValue("t")),
                    //1);

            builder.setSpout(
                    "kafka",
                    new TestWordSpout(),
                    1);

            builder.setBolt(
                    "clean",
                    new TweetCleanBolt(),
                    1)
                    .shuffleGrouping("kafka");

            Config conf = new Config();
            conf.setMaxSpoutPending(5000);
            conf.setStatsSampleRate(1.0d);
            conf.setDebug(true);
            conf.setNumWorkers(1);

            StormSubmitter.submitTopology("storm-processors", conf, builder.createTopology());
        } catch (ParseException exception) {
            System.out.print("parse error: ");
            System.out.println(exception.getMessage());
        }
    }
}
