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
package io.github.ognis1205.mutad.storm.mappers;

import java.util.ArrayList;
import java.util.List;
import io.github.ognis1205.mutad.storm.beans.Geo;
import io.github.ognis1205.mutad.storm.beans.LonLat;
import io.github.ognis1205.mutad.storm.beans.Tweet;


/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Tweet2Geo {
    /**
     * Maps `Tweet` to `Geo`.
     * @param tweet `Tweet` instance to be mapped.
     * @return `Geo` instance.
     */
    public static List<Geo> map(Tweet tweet) {
        List<Geo> geos = new ArrayList<>();

        Long id = tweet.getId();
        Long timestamp  = tweet.getTimestamp();
        String lang     = tweet.getLang();
        String userName = tweet.getUserName();
        String userId   = tweet.getUserId();
        String imageUrl = tweet.getImageUrl();
        String text     = tweet.getText();
        List<String> hashtags   = tweet.getHashtags();
        List<String> cityNames  = tweet.getCityNames();
        List<LonLat> cityCoords = tweet.getCityCoords();

        for (int i = 0; i < cityNames.size(); i++) {
            Geo geo = new Geo();
            geo.setId(id);
            geo.setTimestamp(timestamp);
            geo.setLang(lang);
            geo.setUserName(userName);
            geo.setUserId(userId);
            geo.setImageUrl(imageUrl);
            geo.setText(text);
            geo.setHashtags(hashtags);
            geo.setCityName(cityNames.get(i));
            geo.setCityCoord(cityCoords.get(i));
            geos.add(geo);
        }

        return geos;
    }

    /**
     * Maps `Tweet` to `Geo`.
     * @param tweet `Tweet` instance to be mapped.
     * @return `Geo` instance.
     */
    public static Geo copy(Tweet tweet) {
      Geo geo = new Geo();
      geo.setId(tweet.getId());
      geo.setTimestamp(tweet.getTimestamp());
      geo.setLang(tweet.getLang());
      geo.setUserName(tweet.getUserName());
      geo.setUserId(tweet.getUserId());
      geo.setImageUrl(tweet.getImageUrl());
      geo.setText(tweet.getText());
      geo.setHashtags(tweet.getHashtags());
      geo.setCityName("N/A");
      geo.setCityCoord(tweet.getGeo());
      return geo;
    }

    private Tweet2Geo() {}
}
