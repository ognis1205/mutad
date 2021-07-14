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
package io.github.ognis1205.mutad.spring.model;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Document(indexName = "tweet")
public class Tweet {
    @Id
    private String id;

    @Field(name = "timestamp", type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date timestamp;

    @Field(name = "lang", type = FieldType.Keyword)
    private String lang;

    @Field(name = "text", type = FieldType.Text)
    private String text;

    @Field(name = "hashtags", type = FieldType.Keyword)
    private List<String> hashtags;

    @GeoPointField
    private GeoPoint geo;

    /** Constructor. */
    public Tweet() {
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
