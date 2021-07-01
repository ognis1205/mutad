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
package io.github.ognis1205.tweet_visualization.storm.spouts;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class HosebirdSpout extends BaseRichSpout {
    /** SL4J Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(HosebirdSpout.class);

    /** `OutputCollector` instance to expose the API for emitting tuples. */
    SpoutOutputCollector collector;

    /** Blocking queue for storing tweet data. */
    private BlockingQueue<String> que;

    /** Twitter API key of the OAuth. */
    private String apiKey;

    /** Twitter API secret key of the OAuth. */
    private String apiSecret;

    /** Twitter token of the OAuth. */
    private String token;

    /** Twitter secret token of the OAuth. */
    private String tokenSecret;

    /** Twitter Http client. */
    private Client client;

    BlockingQueue<String> msgQueue;

    BlockingQueue<Event> eventQueue;

    /**
     * Initialize `HosebirdSpout` instance.
     * @param apiKey API key of the OAuth.
     * @param apiSecret API secret key of the OAuth.
     * @param token token of the OAuth.
     * @param tokenSecret secret token of the OAuth.
     */
    public HosebirdSpout(String apiKey, String apiSecret, String token, String tokenSecret) {
        this.que = new LinkedBlockingQueue<String>(1000);
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        LOG.info("starting up client to collect tweets from Hosebird...");
        ClientBuilder builder = new ClientBuilder()
                .name("Tweet-Visualization-Hosebird-Client")
                .hosts(new HttpHosts(Constants.STREAM_HOST))
                .authentication(new OAuth1(this.apiKey, this.apiSecret, this.token, this.tokenSecret))
                .endpoint(new StatusesSampleEndpoint())
                .processor(new StringDelimitedProcessor(this.que));
        this.client = builder.build();
        this.client.connect();
        LOG.info("starting up client to collect tweets from Hosebird: done!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        LOG.info("shutting down client from twitter...");
        this.client.stop();
        LOG.info("shutting down client from twitter: done!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextTuple() {
        String tweet = null;
        try {
            tweet = this.que.poll(5, TimeUnit.SECONDS);
            LOG.trace(tweet);
            this.collector.emit(new Values(tweet));
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(Object msgId) {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fail(Object msgId) {
        // Do nothing.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweets"));
    }
}
