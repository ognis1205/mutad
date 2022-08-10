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

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@Document(indexName = "tweet")
public class Topic {
    private String name;

    private Long count;

    /** Constructor. */
    public Topic() {
        // Do nothing.
    }

    /** Constructor. */
    public Topic(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    /** Getter/Setter. */
    public String getName() {
        return this.name;
    }

    /** Getter/Setter. */
    public void setName(String name) {
        this.name = name;
    }

    /** Getter/Setter. */
    public Long getCount() {
        return this.count;
    }

    /** Getter/Setter. */
    public void setCount(Long count) {
        this.count = count;
    }
}
