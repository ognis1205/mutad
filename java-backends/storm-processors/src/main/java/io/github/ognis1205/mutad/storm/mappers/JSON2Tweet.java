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
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.ognis1205.mutad.storm.beans.LonLat;
import io.github.ognis1205.mutad.storm.beans.Tweet;
import io.github.ognis1205.mutad.storm.utils.Texts;


/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class JSON2Tweet {
    /**
     * Maps `String` to `Tweet`.
     * @param json `String` instance to be mapped.
     * @return `Tweet` instance.
     */
    public static Tweet map(String json) {
        return map(new JSONObject(json));
    }

    /**
     * Maps `JSONObject` to `Tweet`.
     * @param json `JSONObject` instance to be mapped.
     * @return `Tweet` instance.
     */
    public static Tweet map(JSONObject json) {
        Tweet tweet = new Tweet();

        try {
            tweet.setId(json.getLong("id"));
        } catch (Exception e) {
            tweet.setId(-1L);
        }

        try {
            tweet.setTimestamp(Long.parseLong(json.getString("timestamp_ms")));
        } catch (Exception e) {
            tweet.setTimestamp(-1L);
        }

        try {
            tweet.setLang(json.getString("lang"));
        } catch (Exception e) {
            tweet.setLang("");
        }

        try {
            tweet.setUserName(json.getJSONObject("user").getString("name"));
        } catch (Exception e) {
            tweet.setUserName("");
        }

        try {
            tweet.setUserId(json.getJSONObject("user").getString("screen_name"));
        } catch (Exception e) {
            tweet.setUserId("");
        }

        try {
            tweet.setImageUrl(json.getJSONObject("user").getString("profile_image_url_https"));
        } catch (Exception e) {
            tweet.setImageUrl("");
        }

        try {
            tweet.setText(Texts.removeEmoji(Texts.removeUrl(json.getString("text"))));
        } catch (Exception e) {
            tweet.setText("");
        }

        try {
            JSONArray hashtags = json.getJSONObject("entities").getJSONArray("hashtags");
            ArrayList<String> tags = new ArrayList<>();
            for (int i = 0; i < hashtags.length(); i++)
                tags.add(hashtags.getJSONObject(i).getString("text"));
            tweet.setHashtags(tags);
        } catch (Exception e) {
            tweet.setHashtags(new ArrayList<>());
        }

        try {
            JSONArray coords = json.getJSONObject("coordinates").getJSONArray("coordinates");
            tweet.setGeo(new LonLat(coords.getDouble(0), coords.getDouble(1)));
        } catch (Exception e) {
            tweet.setGeo(new LonLat());
        }

        tweet.setCityNames(new ArrayList<>());

        tweet.setCityCoords(new ArrayList<>());

        return tweet;
    }

    private JSON2Tweet() {}
}
