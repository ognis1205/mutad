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
import java.util.stream.Collectors;
import org.json.JSONObject;
import io.github.ognis1205.mutad.storm.beans.Tweet;


/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Tweet2JSON {
    /**
     * Maps `Tweet` to `JSONObject`.
     * @param tweet `Tweet` instance to be mapped.
     * @return `JSONObject` instance.
     */
    public static JSONObject map(Tweet tweet) {
        JSONObject json = new JSONObject();

        List<List<Double>> coodinates = tweet.getCityCoords().stream().map(l -> {
            List<Double> ret = new ArrayList<>();
            ret.add(l.getLon());
            ret.add(l.getLat());
            return ret;
        }).collect(Collectors.toList());

        if (coodinates.size() > 0) {
            JSONObject cityCoords = new JSONObject();
            cityCoords.put("type", "multipoint");
            cityCoords.put("coordinates", coodinates);
            json.put("city_coords", cityCoords);
        }

        if (tweet.getGeo().getDefined()) {
            JSONObject geo = new JSONObject();
            geo.put("lon", tweet.getGeo().getLon());
            geo.put("lat", tweet.getGeo().getLat());
            json.put("geo", geo);
        }

        json.put("id",         tweet.getId()                );
        json.put("timestamp",  tweet.getTimestamp()         );
        json.put("lang",       tweet.getLang()              );
        json.put("user_name",  tweet.getUserName()          );
        json.put("user_id",    tweet.getUserId()            );
        json.put("image_url",  tweet.getImageUrl()          );
        json.put("text",       tweet.getText()              );
        json.put("hashtags",   tweet.getHashtags().toArray());
        json.put("city_names", tweet.getCityNames()         );

        return json;
    }

    private Tweet2JSON() {}
}
