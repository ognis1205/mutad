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
package io.github.ognis1205.mutad.spring.service.impl;

import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import io.github.ognis1205.mutad.spring.model.Topic;
import io.github.ognis1205.mutad.spring.model.Tweet;
import io.github.ognis1205.mutad.spring.model.TweetCount;
import io.github.ognis1205.mutad.spring.repository.TweetRepository;
import io.github.ognis1205.mutad.spring.service.TweetService;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
@Service
public class TweetServiceImpl implements TweetService {
    /** `TweetRepository` implementation. */
    @Autowired
    private TweetRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> getAll() {
        return this.repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> getByHashtags(
            Date from,
            Date to,
            String text,
            List<String> hashtags) {
        return this.repository.findByHashtags(from, to, text, hashtags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> getByGeolocation(
            Date from,
            Date to,
            String text,
            List<String> hashtags,
            GeoPoint center,
            String radius) {
        return this.repository.findByGeolocation(from, to, text, hashtags, center, radius);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> getLatest(
            Date before,
            String text,
            List<String> hashtags,
            int page,
            int size) {
        return this.repository.findLatest(before, text, hashtags, page, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Topic> getTopics(
            Date from,
            Date to,
            GeoPoint center,
            String radius,
            int topN) {
        return this.repository.findTopics(from, to, center, radius, topN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TweetCount> getCounts(
            Date from,
            Date to,
            String interval,
            GeoPoint center,
            String radius) {
        return this.repository.aggregateByDate(from, to, interval, center, radius);
    }
}
