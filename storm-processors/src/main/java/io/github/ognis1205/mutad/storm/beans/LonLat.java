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

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class LonLat implements Serializable {
    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Longitude. */
    private Double lon;

    /** Latitude. */
    private Double lat;

    /** Constructor. */
    public LonLat() {
        this.lon = this.lat = 0.0D;
    }

    /** Constructor. */
    public LonLat(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    /** toArray. */
    public List<Double> toArray() {
        List<Double> array = new ArrayList<>();
        array.add(lon);
        array.add(lat);
        return array;
    }

    /** Getter/Setter. */
    public Double getLon() {
        return this.lon;
    }

    /** Getter/Setter. */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /** Getter/Setter. */
    public Double getLat() {
        return this.lat;
    }

    /** Getter/Setter. */
    public void setLat(Double lat) {
        this.lat = lat;
    }
}
