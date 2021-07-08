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
import io.github.ognis1205.tweet_visualization.storm.beans.LonLat;
import io.github.ognis1205.util.nlang.trie.TrieSearcher;
import io.github.ognis1205.util.nlang.dict.Dictionary;
import io.github.ognis1205.util.nlang.dict.Lexeme;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Text2Geo {
    private static class City extends Lexeme<LonLat> {
        /** City name. */
        private String name;

        /** City geos. */
        private List<LonLat> geos;

        /** Constructor. */
        public City (List<String> line) {
            this.name = line.get(0);
            this.geos = new ArrayList<>();
            this.geos.add(new LonLat(Double.parseDouble(line.get(2)), Double.parseDouble(line.get(1))));
        }

        /** Add another geo point. */
        public void add(List<String> line) {
            this.geos.add(new LonLat(Double.parseDouble(line.get(2)), Double.parseDouble(line.get(1))));
        }

        /** {@inheritDoc} */
        @Override
        public String getKey() {
            return this.name.toLowerCase(Locale.ROOT);
        }

        /** {@inheritDoc} */
        @Override
        public LonLat getValue() {
            Double lat = 0.0;
            Double lon = 0.0;
            for (int i = 0; i < this.geos.size(); i++) {
                lat += this.geos.get(i).getLat();
                lon += this.geos.get(i).getLon();
            }
            return new LonLat(lon, lat);
        }
    }

    private static class Matcher implements TrieSearcher.Callback {
        /** Text2Geo module reference. */
        private Text2Geo text2Geo;

        /** Text to be handled. */
        private String text;

        /** Found citie so far. */
        public Map<String, LonLat> found;

        /** Constructor. */
        public Matcher(Text2Geo text2Geo, String text) {
            this.text2Geo = text2Geo;
            this.text = text;
            this.found = new HashMap<>();
        }

        /** Returns found matches as JSON. */
        public List<String> getKeys() {
            List<String> ret = new ArrayList<>();
            for (String key : this.found.keySet()) ret.add(key);
            return ret;
        }

        /** Returns found matches as JSON. */
        public List<LonLat> getValues() {
            List<LonLat> ret = new ArrayList<>();
            for (LonLat val : this.found.values()) ret.add(val);
            return ret;
        }

        /** {@inheritDoc} */
        @Override
        public void apply(int begin, int offset, int id) {
            this.found.put(this.text.substring(begin, begin + offset), this.text2Geo.cities.get(id));
        }
    }

    /** City names to Lon/Lat pairs. */
    protected Dictionary<LonLat> cities;

    /** Match found. */
    private Matcher match;

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
        this.cities = new Dictionary<>(new ArrayList<>(result.values()), false);
    }

    /**
     * Extracts geo information from a given text.
     * @param text the text to be extracted.
     */
    public void match(String text) {
        this.match = new Matcher(this, text);
        for (int curr = 0; curr < text.length(); curr++) this.cities.prefix(text, curr++, match);
    }

    /** Returns found matches as JSON. */
    public List<String> getCityNames() {
        return this.match.getKeys();
    }

    /** Returns found matches as JSON. */
    public List<LonLat> getCityCoords() {
        return this.match.getValues();
    }
}
