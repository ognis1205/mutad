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
package io.github.ognis1205.mutad.storm;

import java.util.Map;
import java.util.HashMap;
import org.elasticsearch.storm.EsBolt;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class EsTweetSinkBuilder {
    /**
     * Instanciate `EsBolt` instance.
     *
     * @param esNodes   Comma-separated Elasticsearch nodes.
     * @param indexType index/type string.
     */
    public static EsBolt build(String esNodes, String indexType) {
        Map<String, Object> conf = new HashMap<>();
        conf.put("es.nodes",                       esNodes);
        conf.put("es.nodes.wan.only",              "true" );
        conf.put("es.input.json",                  "true" );
        conf.put("es.storm.bolt.tick.tuple.flush", "false");
        conf.put("es.batch.size.entries",          "100"  );
        return new EsBolt(indexType, conf);
    }

    private EsTweetSinkBuilder() {}
}