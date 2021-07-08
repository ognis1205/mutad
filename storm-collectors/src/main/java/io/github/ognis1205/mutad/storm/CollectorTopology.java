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

import io.github.ognis1205.mutad.storm.spouts.HosebirdSpout;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import io.github.ognis1205.mutad.storm.sinks.KafkaTweetSink;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class CollectorTopology {
    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option apiKey = Option.builder("a")
                .required(true)
                .hasArg(true)
                .desc("specifies HOSEbird API key")
                .longOpt("hosebird-api-key")
                .build();

        Option apiSecret = Option.builder("b")
                .required(true)
                .hasArg(true)
                .desc("specifies HOSEbird API secret key")
                .longOpt("hosebird-api-secret")
                .build();

        Option tokenKey = Option.builder("c")
                .required(true)
                .hasArg(true)
                .desc("specifies HOSEbird API token")
                .longOpt("hosebird-token")
                .build();

        Option tokenSecret = Option.builder("d")
                .required(true)
                .hasArg(true)
                .desc("specifies HOSEbird API secret token")
                .longOpt("hosebird-token-secret")
                .build();

        Option brokerList = Option.builder("e")
                .required(true)
                .hasArg(true)
                .desc("Kafka broker list in comma-separated format")
                .longOpt("kafka-broker-list")
                .build();

        Option kafkaTopic = Option.builder("f")
                .required(true)
                .hasArg(true)
                .desc("Kafka topic")
                .longOpt("kafka-topic")
                .build();

        options.addOption(apiKey);
        options.addOption(apiSecret);
        options.addOption(tokenKey);
        options.addOption(tokenSecret);
        options.addOption(brokerList);
        options.addOption(kafkaTopic);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, args);
            TopologyBuilder builder = new TopologyBuilder();

            builder.setSpout(
                    "hosebird",
                    new HosebirdSpout(
                            commandLine.getOptionValue("a"),
                            commandLine.getOptionValue("b"),
                            commandLine.getOptionValue("c"),
                            commandLine.getOptionValue("d")),
                    1);

            builder.setBolt(
                    "kafka",
                    new KafkaTweetSink(
                            commandLine.getOptionValue("e"),
                            commandLine.getOptionValue("f")),
                    3)
                    .shuffleGrouping("hosebird");

            Config conf = new Config();
            conf.setNumWorkers(10);
            conf.setDebug(false);

            StormSubmitter.submitTopology("collectors", conf, builder.createTopology());
        } catch (ParseException exception) {
            System.err.print("parse error: ");
            System.err.println(exception.getMessage());
        }
    }
}
