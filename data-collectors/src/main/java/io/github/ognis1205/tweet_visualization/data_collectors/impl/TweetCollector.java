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
package io.github.ognis1205.tweet_visualization.data_collectors.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.extern.slf4j.Slf4j;
import io.github.ognis1205.tweet_visualization.data_collectors.Collector;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
public class TweetCollector implements Collector<String> {
    /** Blocking queue for storing tweet data. */
    private BlockingQueue<String> que;

    /** Twitter Http client. */
    private Client client;

    /**
     * Initialize `TweetCollector` instance.
     * <ul>
     *     <li>Set up your blocking queues: Be sure to the size set properly based on the expected TPS.</li>
     *     <li>Declare the host you want to connect to.</li>
     * </ul>
     * @param apiKey API key of the OAuth.
     * @param apiSecret API secret key of the OAuth.
     * @param token token of the OAuth.
     * @param tokenSecret secret token of the OAuth.
     */
    public TweetCollector(String apiKey, String apiSecret, String token, String tokenSecret) {
        this.que = new LinkedBlockingQueue<String>(1000);
        ClientBuilder builder = new ClientBuilder()
                .name("Tweet-Visualization-Hosebird-Client")
                .hosts(new HttpHosts(Constants.STREAM_HOST))
                .authentication(new OAuth1(apiKey, apiSecret, token, tokenSecret))
                .endpoint(new StatusesSampleEndpoint())
                .processor(new StringDelimitedProcessor(this.que));
        this.client = builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        log.info("starting up client to collect tweets from Hosebird...");
        this.client.connect();
        log.info("starting up client to collect tweets from Hosebird: done!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String collect() {
        String twt = null;
        try {
            twt = this.que.poll(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return twt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        log.info("shutting down client from twitter...");
        this.client.stop();
        log.info("shutting down client from twitter: done!");
    }
}
