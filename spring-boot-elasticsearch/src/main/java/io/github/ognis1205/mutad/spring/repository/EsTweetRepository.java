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

import java.util.Date;
import java.util.List;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import io.github.ognis1205.mutad.spring.model.Tweet;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public interface EsTweetRepository {
    /**
     * Finds all tweet documents in 'tweet' index which have selected hashtags
     * within a given time period.
     * @param from the start date of a given period.
     * @param to the end date of a given period.
     * @param text the text for full text search.
     * @param hashtags the target hashtags.
     * @return the `Tweet` instances match the query condition.
     */
    public List<Tweet> findByHashtags(Date from, Date to, String text, List<String> hashtags);

    /**
     * Finds all tweet documents in 'tweet' index which have selected hashtags
     * within a given time period with a given geolocation.
     * @param from the start date of a given period.
     * @param to the end date of a given period.
     * @param text the text for full text search.
     * @param hashtags the target hashtags.
     * @param center the center geo point to be queried.
     * @param radius the radius of a concerning geo area.
     * @return the `Tweet` instances match the query condition.
     */
    public List<Tweet> findByGeolocation(Date from, Date to, String text, List<String> hashtags, GeoPoint center, String radius);
}
