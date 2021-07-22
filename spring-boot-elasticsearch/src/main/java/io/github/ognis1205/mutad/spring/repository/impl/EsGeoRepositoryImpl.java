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
import io.github.ognis1205.mutad.spring.model.Geo;
import io.github.ognis1205.mutad.spring.repository.EsGeoRepository;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
@Repository
public class EsGeoRepositoryImpl implements EsGeoRepository {
    /** Elasticsearch template implementation. */
    @Autowired
    private ElasticsearchRestTemplate template;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Geo> findByHashtags(
            Date from,
            Date to,
            String text,
            List<String> hashtags) {
        BoolQueryBuilder builder = QueryBuilders
                .boolQuery()
                .must(QueryBuilders
                        .rangeQuery("timestamp")
                        .gte(from.getTime())
                        .lte(to.getTime()));
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
        return this.template.search(query, Geo.class)
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Geo> findByGeolocation(
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
                        .geoDistanceQuery("city_coord")
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
        return this.template.search(query, Geo.class)
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}

