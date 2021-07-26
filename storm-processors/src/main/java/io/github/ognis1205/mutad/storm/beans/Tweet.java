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
import java.util.List;


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

    /** User name. */
    private String userName;

    /** User id. */
    private String userId;

    /** Image URL. */
    private String imageUrl;

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
    public String getUserName() {
        return this.userName;
    }

    /** Getter/Setter. */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Getter/Setter. */
    public String getUserId() {
        return this.userId;
    }

    /** Getter/Setter. */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** Getter/Setter. */
    public String getImageUrl() {
        return this.imageUrl;
    }

    /** Getter/Setter. */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
