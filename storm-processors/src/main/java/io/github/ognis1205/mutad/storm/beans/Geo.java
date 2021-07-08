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

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Geo implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /***/
    public static List<Geo> split(Tweet tweet) {
        List<Geo> geos = new ArrayList<>();

        Long id = tweet.getId();
        Long timestamp = tweet.getTimestamp();
        String lang    = tweet.getLang();
        String text    = tweet.getText();
        List<String> hashtags   = tweet.getHashtags();
        List<String> cityNames  = tweet.getCityNames();
        List<LonLat> cityCoords = tweet.getCityCoords();

        for (int i = 0; i < cityNames.size(); i++) {
            Geo geo = new Geo();
            geo.setId(id);
            geo.setTimestamp(timestamp);
            geo.setLang(lang);
            geo.setText(text);
            geo.setHashtags(hashtags);
            geo.setCityName(cityNames.get(i));
            geo.setCityCoord(cityCoords.get(i));
            geos.add(geo);
        }

        return geos;
    }

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

    /** Tweet city name. */
    private String cityName;

    /** Tweet city coord. */
    private LonLat cityCoord;

    /** Constructor. */
    public Geo() {
        // Do nothing.
    }

    /** toJSON. */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id",         this.getId()                 );
        json.put("timestamp",  this.getTimestamp()          );
        json.put("lang",       this.getLang()               );
        json.put("text",       this.getText()               );
        json.put("hashtags",   this.getHashtags().toArray() );
        json.put("city_name",  this.getCityName()           );
        json.put("city_coord", this.getCityCoord().toArray());
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
    public String getCityName() {
        return this.cityName;
    }

    /** Getter/Setter. */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /** Getter/Setter. */
    public LonLat getCityCoord() {
        return this.cityCoord;
    }

    /** Getter/Setter. */
    public void setCityCoord(LonLat cityCoord) {
        this.cityCoord = cityCoord;
    }
}
