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
package io.github.ognis1205.mutad.storm.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.ognis1205.mutad.storm.utils.Texts;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Tweet implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Tweet identifier. */
    private Long id;

    /** Tweet timestamp. */
    private Long timestamp;

    /** Tweet locale. */
    private String lang;

    /** Tweet text. */
    private String text;

    /** Tweet hashtags. */
    private List<String> hashtags;

    /** Tweet geo. */
    private LonLat geo;

    /** Tweet city names. */
    private List<String> cityNames;

    /** Tweet city coords. */
    private List<LonLat> cityCoords;

    /** Constructor. */
    public Tweet() {
        // Do nothing.
    }

    /** Constructor. */
    public Tweet(String json) {
        this(new JSONObject(json));
    }

    /** Constructor. */
    public Tweet(JSONObject json) {
        try {
            this.id = json.getLong("id");
        } catch (Exception e) {
            this.id = -1L;
        }

        try {
            this.timestamp = Long.parseLong(json.getString("timestamp_ms"));
        } catch (Exception e) {
            this.timestamp = -1L;
        }

        try {
            this.lang = json.getString("lang");
        } catch (Exception e) {
            this.lang = "";
        }

        try {
            this.text = Texts.removeEmoji(Texts.removeUrl(json.getString("text")));
        } catch (Exception e) {
            this.text = "";
        }

        try {
            JSONArray hashtags = json.getJSONObject("entities").getJSONArray("hashtags");
            this.hashtags = new ArrayList<>();
            for (int i = 0; i < hashtags.length(); i++)
                this.hashtags.add(hashtags.getJSONObject(i).getString("text"));
        } catch (Exception e) {
            this.hashtags = new ArrayList<>();
        }

        try {
            JSONArray coords = json.getJSONObject("coordinates").getJSONArray("coordinates");
            this.geo = new LonLat(coords.getDouble(0), coords.getDouble(1));
        } catch (Exception e) {
            this.geo = new LonLat();
        }

        this.cityNames = new ArrayList<>();

        this.cityCoords = new ArrayList<>();
    }

    /** toJSON. */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        List<List<Double>> coodinates = this.getCityCoords().stream().map(l -> {
                List<Double> ret = new ArrayList();
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
        json.put("id",          this.getId()                );
        json.put("timestamp",   this.getTimestamp()         );
        json.put("lang",        this.getLang()              );
        json.put("text",        this.getText()              );
        json.put("hashtags",    this.getHashtags().toArray());
        json.put("geo",         this.getGeo().toArray()     );
        json.put("city_names",  this.getCityNames()         );
        return json;
    }

    /** Getter/Setter. */
    public Long getId() {
        return this.id;
    }

    /** Getter/Setter. */
    public void setId(Long id) {
        this.id = id;
    }

    /** Getter/Setter. */
    public Long getTimestamp() {
        return this.timestamp;
    }

    /** Getter/Setter. */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /** Getter/Setter. */
    public String getLang() {
        return this.lang;
    }

    /** Getter/Setter. */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /** Getter/Setter. */
    public String getText() {
        return this.text;
    }

    /** Getter/Setter. */
    public void setText(String text) {
        this.text = text;
    }

    /** Getter/Setter. */
    public List<String> getHashtags() {
        return this.hashtags;
    }

    /** Getter/Setter. */
    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    /** Getter/Setter. */
    public LonLat getGeo() {
        return this.geo;
    }

    /** Getter/Setter. */
    public void setGeo(LonLat geo) {
        this.geo = geo;
    }

    /** Getter/Setter. */
    public List<String> getCityNames() {
        return this.cityNames;
    }

    /** Getter/Setter. */
    public void setCityNames(List<String> cityNames) {
        this.cityNames = cityNames;
    }

    /** Getter/Setter. */
    public List<LonLat> getCityCoords() {
        return this.cityCoords;
    }

    /** Getter/Setter. */
    public void setCityCoords(List<LonLat> cityCoords) {
        this.cityCoords = cityCoords;
    }
}
