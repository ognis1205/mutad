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
package io.github.ognis1205.mutad.spring.dto;

import java.util.Date;
import java.util.List;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class TweetDTO {
    private String id;

    private Date timestamp;

    private String lang;

    private String userName;

    private String userId;

    private String imageUrl;

    private String text;

    private List<String> hashtags;

    private GeoPoint geo;

    /** Constructor. */
    public TweetDTO() {
        // Do nothing.
    }

    /** Getter/Setter. */
    public String getId() {
        return this.id;
    }

    /** Getter/Setter. */
    public void setId(String id) {
        this.id = id;
    }

    /** Getter/Setter. */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /** Getter/Setter. */
    public void setTimestamp(Date timestamp) {
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
    public GeoPoint getGeo() {
        return this.geo;
    }

    /** Getter/Setter. */
    public void setGeo(GeoPoint geo) {
        this.geo = geo;
    }
}

