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
package io.github.ognis1205.mutad.storm.mappers;

import org.json.JSONObject;
import io.github.ognis1205.mutad.storm.beans.Geo;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Geo2JSON {
    /**
     * Maps `Geo` to `JSONObject`.
     * @param geo `Geo` instance to be mapped.
     * @return `JSONObject` instance.
     */
    public static JSONObject map(Geo geo) {
        JSONObject json    = new JSONObject();
        JSONObject geoJson = new JSONObject();
        geoJson.put("lon", geo.getCityCoord().getLon());
        geoJson.put("lat", geo.getCityCoord().getLat());
        json.put("id",         geo.getId()                );
        json.put("timestamp",  geo.getTimestamp()         );
        json.put("lang",       geo.getLang()              );
        json.put("text",       geo.getText()              );
        json.put("hashtags",   geo.getHashtags().toArray());
        json.put("city_name",  geo.getCityName()          );
        json.put("city_coord", geoJson                    );
        return json;
    }

    private Geo2JSON() {}
}
