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
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;
import io.github.ognis1205.mutad.spring.model.Tweet;
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
        BoolQueryBuilder builder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders
                        .rangeQuery("timestamp")
                        .gte(from.getTime())
                        .lte(to.getTime()))
                .filter(QueryBuilders
                        .termsQuery("hashtags", hashtags));
        if (!text.isEmpty())
            builder.filter(QueryBuilders
                .matchQuery("text", text)
                .analyzer("rebuilt_english")
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .build();
        return this.template.search(query, Tweet.class)
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
        BoolQueryBuilder builder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders
                        .rangeQuery("timestamp")
                        .gte(from.getTime())
                        .lte(to.getTime()))
                .filter(QueryBuilders
                        .geoDistanceQuery("geo")
                        .point(center.getLat(), center.getLon())
                        .distance(radius));
        if (!text.isEmpty())
            builder.filter(QueryBuilders
                    .matchQuery("text", text)
                    .analyzer("rebuilt_english")
                    .fuzziness(Fuzziness.AUTO)
                    .prefixLength(3)
                    .maxExpansions(10));
        if (!hashtags.isEmpty())
            builder.filter(QueryBuilders
                    .termsQuery("hashtags", hashtags));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .build();
        return this.template.search(query, Tweet.class)
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
