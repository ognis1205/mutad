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
package io.github.ognis1205.mutad.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
@Configuration("esProperties")
public class EsProperties {
    /** Elasticsearch URIs. */
    private String uris;

    /** Getter/Setter. */
    public String getUris() {
        return this.uris;
    }

    /** Getter/Setter. */
    public void setUris(String uris) {
        this.uris = uris;
    }
}
