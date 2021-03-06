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
import io.github.ognis1205.mutad.spring.model.Geo;
import io.github.ognis1205.mutad.spring.repository.GeoRepository;
import io.github.ognis1205.mutad.spring.service.GeoService;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Slf4j
@Service
public class GeoServiceImpl implements GeoService {
    /** `GeoRepository` implementation. */
    @Autowired
    private GeoRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Geo> getAll() {
        return this.repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Geo> getByHashtags(
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
    public List<Geo> getByGeolocation(
            Date from,
            Date to,
            String text,
            List<String> hashtags,
            GeoPoint center,
            String radius) {
        return this.repository.findByGeolocation(from, to, text, hashtags, center, radius);
    }
}
