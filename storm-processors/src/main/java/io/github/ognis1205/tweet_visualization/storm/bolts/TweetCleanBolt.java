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
package io.github.ognis1205.tweet_visualization.storm.bolts;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
public class TweetCleanBolt extends BaseRichBolt {
    /** `OutputCollector` instance to expose the API for emitting tuples. */
    OutputCollector collector;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Tuple tuple) {
        String topic = tuple.getString(0);
//        String key = tuple.getString(1);
//        String value = tuple.getString(2);
        log.info("TOPIC: " + topic);
//        log.info("KEY:   " + key);
//        log.info("VALUE: " + value);
        this.collector.emit(tuple, new Values(topic));
        this.collector.ack(tuple);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("exclamated-word"));
    }
}
