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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import com.vdurmont.emoji.EmojiParser;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class TweetCleanBolt extends BaseRichBolt {
    /** SL4J Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TweetCleanBolt.class);

    /** Regex pattern for URLs. */
    private static final Pattern urlPattern = Pattern.compile(
            "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
            Pattern.CASE_INSENSITIVE);

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
        JSONObject tweet = new JSONObject(tuple.getString(0));
        LOG.trace(tweet.toString());
        JSONObject json = this.parse(tweet);
        LOG.trace(json.toString());
        if (json.has("id") && json.has("text")) {
            this.collector.emit(tuple, new Values(json));
            this.collector.ack(tuple);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("json"));
    }

    /**
     * Parses a given `JSONObject` and converts it into a datum in Elasticsearch schema form.
     * @return the corresponding datum form of a given data.
     */
    private JSONObject parse(JSONObject tweet) {
        JSONObject json = new JSONObject();

        try {
            json.put("id", tweet.getLong("id"));
        } catch (Exception e) {
            // Do nothing.
        }

        try {
            json.put("timestamp", Long.parseLong(tweet.getString("timestamp_ms")));
        } catch (Exception e) {
            // Do nothing.
        }

        try {
            json.put("text", this.removeEmoji(this.removeUrl(tweet.getString("text"))));
        } catch (Exception e) {
            // Do nothing.
        }

        try {
            JSONArray coords = tweet.getJSONObject("coordinates").getJSONArray("coordinates");
            JSONObject result = new JSONObject();
            result.put("lon", coords.getFloat(0));
            result.put("lat", coords.getFloat(1));
            json.put("geo", result);
        } catch (Exception e) {
            // Do nothing.
        }

        try {
            JSONArray hashtags = tweet.getJSONObject("entities").getJSONArray("hashtags");
            JSONArray result = new JSONArray();
            for (int i = 0; i < hashtags.length(); i++) {
                JSONObject hashtag = hashtags.getJSONObject(i);
                result.put(hashtag.getString("text"));
            }
            json.put("hashtags", result);
        } catch (Exception e) {
            // Do nothing.
        }

        return json;
    }

    /**
     * Remove all appearances of URLs from the given string.
     * @param str string to be cleaned.
     * @return URLs removed string.
     */
    private String removeUrl(String str) {
        Matcher match = urlPattern.matcher(str);
        int i = 0;
        while (match.find()) {
            str = str.replaceAll(match.group(i++),"").trim();
        }
        return str;
    }

    /**
     * Remove all appearances of emojis from the given string.
     * @param str string to be cleaned.
     * @return emoji removed string.
     */
    private String removeEmoji(String str) {
        return EmojiParser.removeAllEmojis(str);
    }
}
