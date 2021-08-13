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
package io.github.ognis1205.mutad.spring.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.github.ognis1205.mutad.spring.model.Topic;
import io.github.ognis1205.mutad.spring.model.Tweet;
import io.github.ognis1205.mutad.spring.dto.TopicDTO;
import io.github.ognis1205.mutad.spring.dto.TweetDTO;
import io.github.ognis1205.mutad.spring.filter.TopicFilter;
import io.github.ognis1205.mutad.spring.filter.TweetFilter;
import io.github.ognis1205.mutad.spring.mapper.TopicMapper;
import io.github.ognis1205.mutad.spring.mapper.TweetMapper;
import io.github.ognis1205.mutad.spring.service.TweetService;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(TweetController.BASE_URL)
public class TweetController {
    /** Base URL. */
    public static final String BASE_URL = "api/v3/tweet";

    /** `TweetService` instance. */
    @Autowired
    private TweetService service;

    /** `TweetMapper` instance. */
    @Autowired
    private TweetMapper tweetMapper;

    /** `TopicMapper` instance. */
    @Autowired
    private TopicMapper topicMapper;

    /**
     * Gets all tweet documents in 'tweet' index.
     * @return the all `TweetDTO` instances.
     */
    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDTO> getAll() {
        List<Tweet> tweets = this.service.getAll();
        return this.tweetMapper.convert(tweets);
    }

    /**
     * Gets all tweet documents in 'tweet' index which have selected hashtags
     * within a given time period.
     * @param filter the search condition.
     * @return the `TweetDTO` instances match the query condition.
     */
    @PostMapping("/hashtags")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDTO> getByHashtags(@RequestBody TweetFilter filter) {
        List<Tweet> tweets = this.service.getByHashtags(
                filter.getFrom(),
                filter.getTo(),
                filter.getText(),
                filter.getHashtags());
        return this.tweetMapper.convert(tweets);
    }

    /**
     * Returns all tweet documents in 'tweet' index which have selected hashtags
     * within a given time period with a given geolocation.
     * @param filter the search condition.
     * @return the `TweetDTO` instances match the query condition.
     */
    @PostMapping("/geo")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDTO> getByGeolocation(@RequestBody TweetFilter filter) {
        List<Tweet> tweets = this.service.getByGeolocation(
                filter.getFrom(),
                filter.getTo(),
                filter.getText(),
                filter.getHashtags(),
                filter.getCenter(),
                filter.getRadius());
        return this.tweetMapper.convert(tweets);
    }

    /**
     * Returns the top most recent tweet documents in 'tweet' index.
     * within a given time period with a given geolocation.
     * @param filter the search condition.
     * @return the `TweetDTO` instances match the query condition.
     */
    @PostMapping("/latest")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public List<TweetDTO> getLatest(@RequestBody TweetFilter filter) throws Exception {
        List<Tweet> tweets = this.service.getLatest(
                filter.getBefore(),
                filter.getText(),
                filter.getHashtags(),
                filter.getPage(),
                filter.getSize());
        return this.tweetMapper.convert(tweets);
    }

    /**
     * Returns the most hot topics/hashtags in 'tweet' index.
     * within a given time period with a given geolocation.
     * @param filter the search condition.
     * @return the `TopicDTO` instances match the query condition.
     */
    @PostMapping("/topics")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public List<TopicDTO> getTopics(@RequestBody TopicFilter filter) throws Exception {
        List<Topic> topics = this.service.getTopics(
                filter.getFrom(),
                filter.getTo(),
                filter.getCenter(),
                filter.getRadius(),
                filter.getTopN());
        return this.topicMapper.convert(topics);
    }
}
