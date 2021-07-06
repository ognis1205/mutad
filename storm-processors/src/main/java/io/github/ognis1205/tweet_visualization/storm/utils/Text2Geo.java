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
package io.github.ognis1205.tweet_visualization.storm.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.ognis1205.util.nlang.trie.TrieSearcher;
import io.github.ognis1205.util.nlang.dict.Dictionary;
import io.github.ognis1205.util.nlang.dict.Lexeme;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Text2Geo {
    /** SL4J Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(Text2Geo.class);

    private static class City extends Lexeme<JSONArray> {
        /** City name. */
        private String name;

        /** City geos. */
        private JSONArray geos;

        /** Constructor. */
        public City (List<String> line) {
            this.name = line.get(0);
            this.geos = new JSONArray();
            JSONObject geo = new JSONObject();
            geo.put("lat", Double.parseDouble(line.get(1)));
            geo.put("lon", Double.parseDouble(line.get(2)));
            this.geos.put(geo);
        }

        /** Add another geo point. */
        public void add(List<String> line) {
            JSONObject geo = new JSONObject();
            geo.put("lat", Double.parseDouble(line.get(1)));
            geo.put("lon", Double.parseDouble(line.get(2)));
            this.geos.put(geo);
        }

        /** {@inheritDoc} */
        @Override
        public String getKey() {
            return this.name.toLowerCase(Locale.ROOT);
        }

        /** {@inheritDoc} */
        @Override
        public JSONArray getValue() {
            JSONArray geo = new JSONArray();
            Double lat = 0.0;
            Double lon = 0.0;
            for (int i = 0; i < this.geos.length(); i++) {
                JSONObject entry = this.geos.getJSONObject(i);
                lat += entry.getDouble("lat");
                lon += entry.getDouble("lon");
            }
            geo.put(lon / this.geos.length());
            geo.put(lat / this.geos.length());
            //geo.put("lat", lat / this.geos.length());
            //geo.put("lon", lon / this.geos.length());
            return geo;
        }
    }

    private static class Matcher implements TrieSearcher.Callback {
        /** Text2Geo module reference. */
        private Text2Geo text2Geo;

        /** Text to be handled. */
        private String text;

        /** Found citie so far. */
        public Map<String, JSONArray> found;

        /** Constructor. */
        public Matcher(Text2Geo text2Geo, String text) {
            this.text2Geo = text2Geo;
            this.text = text;
            this.found = new HashMap<>();
        }

        /** Returns found matches as JSON. */
        public JSONArray toJSON() {
            JSONArray ret = new JSONArray();
            for (JSONArray arr : this.found.values()) {
                ret.put(arr);
            }
            return ret;
        }

        /** {@inheritDoc} */
        @Override
        public void apply(int begin, int offset, int id) {
            this.found.put(this.text.substring(begin, begin + offset), this.text2Geo.cities.get(id));
        }
    }

    /** City names to Lon/Lat pairs. */
    protected Dictionary<JSONArray> cities;

    /**
     * Instanciate `TExt2Geo` instance.
     * @param csv the path to world cities CSV file.
     */
    public Text2Geo(InputStream csv) throws IOException {
        Map<String, City> result = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csv))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s*,\\s*");
                if (result.containsKey(values[0])) {
                    result.get(values[0].toLowerCase(Locale.ROOT)).add(Arrays.asList(values));
                } else {
                    result.put(values[0].toLowerCase(Locale.ROOT), new City(Arrays.asList(values)));
                }
            }
        }
        this.cities = new Dictionary<JSONArray>(new ArrayList<>(result.values()), false);
    }

    /**
     * Extracts geo information from a given text.
     * @param text the text to be extracted.
     * @return JSONArray consisting of found geo coordinates.
     */
    public JSONArray extract(String text) {
        Matcher match = new Matcher(this, text);
        for (int curr = 0; curr < text.length(); curr++) this.cities.prefix(text, curr++, match);
        return match.toJSON();
    }
}
