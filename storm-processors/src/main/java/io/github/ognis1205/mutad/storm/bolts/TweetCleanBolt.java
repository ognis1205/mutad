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
import java.util.List;
import java.util.Locale;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.ognis1205.mutad.storm.KafkaTweetSpoutBuilder;
import io.github.ognis1205.mutad.storm.beans.Geo;
import io.github.ognis1205.mutad.storm.beans.Tweet;
import io.github.ognis1205.mutad.storm.utils.GeoParser;
import io.github.ognis1205.mutad.storm.utils.impl.ClavinGeoParser;
//import io.github.ognis1205.mutad.storm.utils.impl.TrieGeoParser;

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
            //this.parser = new TrieGeoParser(getClass().getResourceAsStream("/worldcities.csv"));
            this.parser = new ClavinGeoParser(
                    new ApacheExtractor(),
                    new LuceneGazetteer(new File("/CLAVIN-clavin-2.1.0/IndexDirectory")),
                    3,
                    5,
                    true);
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
            LOG.trace(tweet.toJSON().toString());
            if (tweet.getId() > -1 && !tweet.getText().isEmpty() && tweet.getLang().equals("en")) {
                this.collector.emit(TWEET_STREAM, tuple, new Values(tweet.toJSON()));
                this.parse(tweet);
                List<Geo> geos = Geo.split(tweet);
                for (Geo geo : geos) {
                    LOG.trace(geo.toJSON().toString());
                    this.collector.emit(GEO_STREAM, tuple, new Values(geo.toJSON()));
                }
                //this.collector.ack(tuple);
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
            List<GeoParser.Location> locs = this.parser.parse(tweet.getText().toLowerCase(Locale.ROOT));
            tweet.setCityNames(locs.stream().map(l -> l.getName()).collect(Collectors.toList()));
            tweet.setCityCoords(locs.stream().map(l -> l.getLonLat()).collect(Collectors.toList()));
        } catch (Exception e) {
            // Do nothing.
        }
    }
}
