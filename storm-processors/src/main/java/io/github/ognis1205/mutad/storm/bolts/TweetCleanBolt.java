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

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bericotech.clavin.extractor.ApacheExtractor;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.ognis1205.mutad.storm.KafkaTweetSpoutBuilder;
import io.github.ognis1205.mutad.storm.beans.Geo;
import io.github.ognis1205.mutad.storm.beans.Tweet;
import io.github.ognis1205.mutad.storm.mappers.Geo2JSON;
import io.github.ognis1205.mutad.storm.mappers.JSON2Tweet;
import io.github.ognis1205.mutad.storm.mappers.Tweet2Geo;
import io.github.ognis1205.mutad.storm.mappers.Tweet2JSON;
import io.github.ognis1205.mutad.storm.utils.GeoParser;
import io.github.ognis1205.mutad.storm.utils.impl.ClavinGeoParser;

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
    private OutputCollector collector;

    /** `GeoParser` instance to parse geo locations. */
    private GeoParser parser;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        try {
            this.collector = collector;
            URL url =new URL("file:///IndexDirectory");
            File index = Paths.get(url.toURI()).toFile();
            this.parser = new ClavinGeoParser(
                    new ApacheExtractor(),
                    new LuceneGazetteer(index),
                    1,
                    1,
                    false);
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
            Tweet tweet = JSON2Tweet.map(json);
            if (tweet.getId() > -1 && !tweet.getText().isEmpty() && tweet.getLang().equals("en")) {
                this.parse(tweet);
                JSONObject tweetJSON = Tweet2JSON.map(tweet);
                LOG.trace(tweetJSON.toString());
                this.collector.emit(TWEET_STREAM, tuple, new Values(tweetJSON));
                List<Geo> geos = Tweet2Geo.map(tweet);
                for (Geo geo : geos) {
                    JSONObject geoJSON = Geo2JSON.map(geo);
                    LOG.trace(geoJSON.toString());
                    this.collector.emit(GEO_STREAM, tuple, new Values(geoJSON));
                }
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
            List<GeoParser.Location> locs = this.parser.parse(tweet.getText());
            tweet.setCityNames(locs.stream().map(l -> l.getName()).collect(Collectors.toList()));
            tweet.setCityCoords(locs.stream().map(l -> l.getLonLat()).collect(Collectors.toList()));
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
