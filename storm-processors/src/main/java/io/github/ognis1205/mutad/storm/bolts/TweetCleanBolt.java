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
package io.github.ognis1205.mutad.storm.bolts;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.ognis1205.mutad.storm.beans.Geo;
import io.github.ognis1205.mutad.storm.beans.Tweet;
import io.github.ognis1205.mutad.storm.KafkaTweetSpoutBuilder;
import io.github.ognis1205.mutad.storm.utils.Text2Geo;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class TweetCleanBolt extends BaseRichBolt {
    /** SL4J Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TweetCleanBolt.class);

    /** Field name. */
    public static final String FIELD = "json";

    /** Stream name. */
    public static final String TWEET_STREAM = "tweet";

    /** Stream name. */
    public static final String GEO_STREAM = "geo";

    /** `OutputCollector` instance to expose the API for emitting tuples. */
    OutputCollector collector;

    /** `OutputCollector` instance to expose the API for emitting tuples. */
    Text2Geo text2Geo;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        try {
            this.collector = collector;
            this.text2Geo = new Text2Geo(getClass().getResourceAsStream("/worldcities.csv"));
        } catch (Exception e) {
            LOG.error("something bad happened", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Tuple tuple) {
        String json = tuple.getStringByField(KafkaTweetSpoutBuilder.FIELD);
        if (json != null && !json.isEmpty()) {
            Tweet tweet = new Tweet(json);
            this.parse(tweet);
            LOG.trace(tweet.toJSON().toString());
            if (tweet.getId() > -1 && !tweet.getText().isEmpty()) {
                this.collector.emit(TWEET_STREAM, tuple, new Values(tweet.toJSON()));
                List<Geo> geos = Geo.split(tweet);
                for (Geo geo : geos) this.collector.emit(GEO_STREAM, tuple, new Values(geo.toJSON()));
                this.collector.ack(tuple);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(TWEET_STREAM, new Fields(FIELD));
        declarer.declareStream(GEO_STREAM,   new Fields(FIELD));
    }

    /**
     * Parses a given `JSONObject` and converts it into a datum in Elasticsearch schema form.
     * @return the corresponding datum form of a given data.
     */
    private void parse(Tweet tweet) {
        try {
            this.text2Geo.match(tweet.getText().toLowerCase(Locale.ROOT));
            tweet.setCityNames(this.text2Geo.getCityNames());
            tweet.setCityCoords(this.text2Geo.getCityCoords());
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
