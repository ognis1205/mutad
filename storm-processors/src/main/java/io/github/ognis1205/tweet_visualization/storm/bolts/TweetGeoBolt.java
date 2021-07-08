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
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class TweetGeoBolt extends BaseRichBolt {
    /** SL4J Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TweetGeoBolt.class);

    /** Field name. */
    public static final String FIELD = "json";

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
        JSONObject tweet = (JSONObject) tuple.getValueByField(TweetCleanBolt.FIELD);
        JSONArray json = this.parse(tweet);
        LOG.trace(json.toString());
        this.collector.emit(tuple, new Values(new JSONObject()));
        //for (int i = 0; i < json.length(); i++) this.collector.emit(tuple, new Values(json.getJSONObject(i)));
        this.collector.ack(tuple);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("geo"));
    }

    /**
     * Parses a given `JSONObject` and converts it into a datum in Elasticsearch schema form.
     * @return the corresponding datum form of a given data.
     */
    private JSONArray parse(JSONObject tweet) {
        JSONArray json = new JSONArray();

        try {
//            JSONArray names = tweet.getJSONArray("city_names");
//            JSONArray coords = tweet.getJSONObject("city_coords").getJSONArray("coordinates");
//            for (int i = 0; i < names.length(); i++) {
                JSONObject result = new JSONObject();
                JSONObject coord = new JSONObject();
//                coord.put("lon", coords.getJSONArray(i).getFloat(0));
//                coord.put("lat", coords.getJSONArray(i).getFloat(1));
                result.put("id", tweet.getLong("id"));
                result.put("timestamp", Long.parseLong(tweet.getString("timestamp")));
//                result.put("city_name", names.getString(i));
//                result.put("city_coord", coord);
                json.put(result);
//            }
        } catch (Exception e) {
//            LOG.trace("");
            // Do nothing.
        }

        return json;
    }
}

