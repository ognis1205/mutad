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

import io.github.ognis1205.mutad.storm.beans.LonLat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.json.JSONObject;
import io.github.ognis1205.mutad.storm.beans.Tweet;
import io.github.ognis1205.mutad.storm.bolts.TweetCleanBolt;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class ProcessorTopology {
    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option kafkaServers = Option.builder("a")
                .required(true)
                .hasArg(true)
                .desc("specifies Kafka bootstrap servers in csv format")
                .longOpt("kafka-broker-list")
                .build();

        Option kafkaTopic = Option.builder("b")
                .required(true)
                .hasArg(true)
                .desc("specifies Kafka topic")
                .longOpt("kafka-topic")
                .build();

        Option esNodes = Option.builder("c")
                .required(true)
                .hasArg(true)
                .desc("specifies Elasticsearch node servers in csv format")
                .longOpt("es-node-list")
                .build();

        options.addOption(kafkaServers);
        options.addOption(kafkaTopic);
        options.addOption(esNodes);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, args);
            TopologyBuilder builder = new TopologyBuilder();

            builder.setSpout(
                    "kafka",
                    KafkaTweetSpoutBuilder.build(
                            commandLine.getOptionValue("a"),
                            commandLine.getOptionValue("b"),
                            "storm-processor"),
                    1);

            builder.setBolt(
                    "clean",
                    new TweetCleanBolt(),
                    1)
                    .shuffleGrouping("kafka");

//            builder.setBolt(
//                    "geo",
//                    new TweetGeoBolt(),
//                    1)
//                    .shuffleGrouping("clean");

            builder.setBolt(
                    "es-tweet",
                    EsTweetSinkBuilder.build(
                            commandLine.getOptionValue("c"),
                            "tweet"),
                    1)
                    .shuffleGrouping("clean");

/*            builder.setBolt(
                    "es-geo",
                    EsTweetSinkBuilder.build(
                            commandLine.getOptionValue("c"),
                            "geo"),
                    1)
                    .shuffleGrouping("geo");*/

            Config conf = new Config();
            conf.registerSerialization(LonLat.class);
            conf.registerSerialization(Tweet.class);
            conf.registerSerialization(JSONObject.class);
            conf.setMaxSpoutPending(5000);
            conf.setDebug(false);

            StormSubmitter.submitTopology("processors", conf, builder.createTopology());
        } catch (ParseException exception) {
            System.err.print("parse error: ");
            System.err.println(exception.getMessage());
        }
    }
}
