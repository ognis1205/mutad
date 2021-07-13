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
package io.github.ognis1205.mutad.storm.utils;

import java.util.List;
import java.util.Locale;
import io.github.ognis1205.mutad.storm.beans.LonLat;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public interface GeoParser {
    public static class Location {
        /** City name. */
        private String name;

        /** City geos. */
        private LonLat lonLat;

        /** Constructor. */
        public Location (String name, LonLat lonLat) {
            this.name = name;
            this.lonLat = lonLat;
        }

        /** Returns location name. */
        public String getName() {
            return this.name;
        }

        /** Returns location lon/lat. */
        public LonLat getLonLat() {
            return this.lonLat;
        }
    }

    /**
     * Extracts geo information from a given text.
     * @param text the text to be extracted.
     */
    public List<Location> parse(String text);
}
