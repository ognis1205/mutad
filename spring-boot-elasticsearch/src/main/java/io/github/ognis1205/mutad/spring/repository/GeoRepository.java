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
package io.github.ognis1205.mutad.spring.repository;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import io.github.ognis1205.mutad.spring.model.Geo;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public interface GeoRepository extends ElasticsearchRepository<Geo, String>, EsGeoRepository {
    /**
     * Finds all geo documents in 'geo' index.
     * @return the `Geo` instances match the query condition.
     */
    public List<Geo> findAll();
}

