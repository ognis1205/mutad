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
package io.github.ognis1205.mutad.storm.utils.impl;

import java.util.ArrayList;
import java.util.List;
import com.bericotech.clavin.extractor.LocationExtractor;
import com.bericotech.clavin.extractor.LocationOccurrence;
import com.bericotech.clavin.gazetteer.query.Gazetteer;
import com.bericotech.clavin.resolver.ClavinLocationResolver;
import com.bericotech.clavin.resolver.ResolvedLocation;
import io.github.ognis1205.mutad.storm.beans.LonLat;
import io.github.ognis1205.mutad.storm.utils.GeoParser;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class ClavinGeoParser implements GeoParser {
    /** Entity extractor to find location names in text. */
    private LocationExtractor extractor;

    /** Resolver to match location names against gazetteer records. */
    private ClavinLocationResolver resolver;

    /** Maximum hit depth for CLAVIN searches. */
    private int maxHitDepth;

    /** Maximum context window for CLAVIN searches. */
    private int maxContextWindow;

    /** Switch controlling use of fuzzy matching. */
    private final boolean fuzzy;

    /**
     * Instanciate `ClavinGeoParser` instance.
     * @param extractor extracts location names from text
     * @param gazetteer resolves location names to gazetteer
     * @param maxHitDepth the maximum hit depth
     * @param maxContextWindow the maximum context window
     * @param fuzzy switch to turn on/off fuzzy matching
     */
    public ClavinGeoParser(LocationExtractor extractor, Gazetteer gazetteer, int maxHitDepth, int maxContextWindow, boolean fuzzy) {
        this.extractor = extractor;
        this.resolver = new ClavinLocationResolver(gazetteer);
        this.maxHitDepth = maxHitDepth;
        this.maxContextWindow = maxContextWindow;
        this.fuzzy = fuzzy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Location> parse(String text) {
        try {
            List<LocationOccurrence> occurs = this.extractor.extractLocationNames(text);
            List<ResolvedLocation> locs = resolver.resolveLocations(
                    occurs,
                    this.maxHitDepth,
                    this.maxContextWindow,
                    this.fuzzy,
                    ClavinLocationResolver.DEFAULT_ANCESTRY_MODE);
            List<Location> ret = new ArrayList<>();
            for (ResolvedLocation loc : locs) {
                ret.add(new Location(
                        loc.getGeoname().getAsciiName(),
                        new LonLat(loc.getGeoname().getLongitude(), loc.getGeoname().getLatitude())));
            }
            return ret;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
