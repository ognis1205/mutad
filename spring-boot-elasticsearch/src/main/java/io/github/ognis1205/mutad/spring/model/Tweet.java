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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "tweet")
public class Tweet {
    @Id
    private String id;

    @Field(name = "timestamp", type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date timestamp;

    @Field(name = "lang", type = FieldType.Text)
    private String lang;

    @Field(name = "text", type = FieldType.Text)
    private String text;

    @Field(name = "hashtags", type = FieldType.Keyword)
    private List<String> hashtags;

    @GeoPointField
    private GeoPoint geo;
}
