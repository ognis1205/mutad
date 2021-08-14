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
package io.github.ognis1205.mutad.spring.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;
import io.github.ognis1205.mutad.spring.model.Topic;
import io.github.ognis1205.mutad.spring.model.Tweet;
import io.github.ognis1205.mutad.spring.model.TweetCount;
import io.github.ognis1205.mutad.spring.repository.EsTweetRepository;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
@Repository
public class EsTweetRepositoryImpl implements EsTweetRepository {
    /** Elasticsearch template implementation. */
    @Autowired
    private ElasticsearchRestTemplate template;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> findByHashtags(
            Date from,
            Date to,
            String text,
            List<String> hashtags) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders
                                .rangeQuery("timestamp")
                                .gte(from.getTime())
                                .lte(to.getTime())));
        if (!text.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                .matchQuery("text", text)
                .analyzer("rebuilt_english")
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10));
        if (!hashtags.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .termsQuery("hashtags", hashtags));
        return this.template.search(queryBuilder.build(), Tweet.class)
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> findByGeolocation(
            Date from,
            Date to,
            String text,
            List<String> hashtags,
            GeoPoint center,
            String radius) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders
                                .rangeQuery("timestamp")
                                .gte(from.getTime())
                                .lte(to.getTime())))
                .withFilter(QueryBuilders
                        .geoDistanceQuery("geo")
                        .point(center.getLat(), center.getLon())
                        .distance(radius));
        if (!text.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .matchQuery("text", text)
                    .analyzer("rebuilt_english")
                    .fuzziness(Fuzziness.AUTO)
                    .prefixLength(3)
                    .maxExpansions(10));
        if (!hashtags.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .termsQuery("hashtags", hashtags));
        return this.template.search(queryBuilder.build(), Tweet.class)
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tweet> findLatest(
            Date before,
            String text,
            List<String> hashtags,
            int page,
            int size) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders
                                .rangeQuery("timestamp")
                                .lte(before.getTime())))
                .withSort(SortBuilders.fieldSort("timestamp").order(SortOrder.DESC))
                .withPageable(new OffsetLimit(0, page, size));
        if (!text.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .matchQuery("text", text)
                    .analyzer("rebuilt_english")
                    .fuzziness(Fuzziness.AUTO)
                    .prefixLength(3)
                    .maxExpansions(10));
        if (!hashtags.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .termsQuery("hashtags", hashtags));
        return this.template.search(queryBuilder.build(), Tweet.class)
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Topic> findTopics(Date from, Date to, GeoPoint center, String radius, int topN) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders
                                .rangeQuery("timestamp")
                                .gte(from.getTime())
                                .lte(to.getTime())))
                .addAggregation(AggregationBuilders
                        .terms("topics")
                        .field("hashtags")
                        .size(topN));
        if (center != null && radius != null && !radius.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .geoDistanceQuery("geo")
                    .point(center.getLat(), center.getLon())
                    .distance(radius));
        return this.template.search(queryBuilder.build(), Topic.class)
                .getAggregations()
                .<Terms>get("topics")
                .getBuckets()
                .stream()
                .map(bucket -> new Topic(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TweetCount> aggregate(
            Date from,
            Date to,
            String interval,
            GeoPoint center,
            String radius) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders
                                .rangeQuery("timestamp")
                                .gte(from.getTime())
                                .lte(to.getTime())))
                .addAggregation(AggregationBuilders
                        .dateHistogram("counts")
                        .field("timestamp")
                        .calendarInterval(new DateHistogramInterval(interval)));
        if (center != null && radius != null && !radius.isEmpty())
            queryBuilder.withFilter(QueryBuilders
                    .geoDistanceQuery("geo")
                    .point(center.getLat(), center.getLon())
                    .distance(radius));
        return this.template.search(queryBuilder.build(), TweetCount.class)
                .getAggregations()
                .<Histogram>get("counts")
                .getBuckets()
                .stream()
                .map(bucket -> new TweetCount(new Date(((ZonedDateTime) bucket.getKey()).toInstant().toEpochMilli()), bucket.getDocCount()))
                .collect(Collectors.toList());
    }
}
