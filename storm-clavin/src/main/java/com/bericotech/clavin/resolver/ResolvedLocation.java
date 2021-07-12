package com.bericotech.clavin.resolver;

import static com.bericotech.clavin.util.DamerauLevenshtein.damerauLevenshteinDistanceCaseInsensitive;

import com.bericotech.clavin.extractor.LocationOccurrence;
import com.bericotech.clavin.gazetteer.GeoName;

/*#####################################################################
 *
 * CLAVIN (Cartographic Location And Vicinity INdexer)
 * ---------------------------------------------------
 *
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 *
 * ====================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * ====================================================================
 *
 * ResolvedLocation.java
 *
 *###################################################################*/

/**
 * Object produced by resolving a location name against gazetteer
 * records.
 *
 * Encapsulates a {@link com.bericotech.clavin.gazetteer.GeoName} object representing the best match
 * between a given location name and gazetter record, along with some
 * information about the geographic entity resolution process.
 *
 */
public class ResolvedLocation {
    // geographic entity resolved from location name
    private final GeoName geoname;

    // original location name extracted from text
    private final LocationOccurrence location;

    // name from gazetteer record that the inputName was matched against
    private final String matchedName;

    // whether fuzzy matching was used
    private final boolean fuzzy;

    // confidence score for resolution
    private final float confidence;

    /**
     * Builds a {@link ResolvedLocation} from a document retrieved from
     * the Lucene index representing the geographic entity resolved
     * from a location name.
     *
     * @param location      the original location occurrence
     * @param geoname       the matched gazetteer record
     * @param matchedName   the name that was matched by the search engine
     * @param fuzzy         was this a fuzzy match?
     */
    public ResolvedLocation(final LocationOccurrence location, final GeoName geoname, final String matchedName, final boolean fuzzy) {
        this.geoname = geoname;
        this.location = location;
        this.matchedName = matchedName;
        this.fuzzy = fuzzy;

        // for fuzzy matches, confidence is based on the edit distance
        // between the given location name and the matched name
        this.confidence = fuzzy ? 1 / (damerauLevenshteinDistanceCaseInsensitive(location.getText(), matchedName) + (float)0.5) : 1;
        /// TODO: fix this confidence score... it doesn't fully make sense
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.geoname != null ? this.geoname.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResolvedLocation other = (ResolvedLocation) obj;
        if (this.geoname != other.geoname && (this.geoname == null || !this.geoname.equals(other.geoname))) {
            return false;
        }
        return true;
    }

    /**
     * For pretty-printing.
     *
     */
    @Override
    public String toString() {
        return String.format("Resolved \"%s\" as: \"%s\" {%s}, position: %s, confidence: %f, fuzzy: %s",
                location.getText(), matchedName, geoname, location.getPosition(), confidence, fuzzy);
    }

    /**
     * Get the geographic entity resolved from the location name.
     * @return the geographic entity
     */
    public GeoName getGeoname() {
        return geoname;
    }

    /**
     * Get the original location name extracted from the text.
     * @return the original occurrence of the location name
     */
    public LocationOccurrence getLocation() {
        return location;
    }

    /**
     * Get the name from the gazetteer record that the inputName was
     * matched against.
     * @return the matched name
     */
    public String getMatchedName() {
        return matchedName;
    }

    /**
     * Was fuzzy matching used?
     * @return <code>true</code> if fuzzy matching was used
     */
    public boolean isFuzzy() {
        return fuzzy;
    }

    /**
     * Get the confidence score for resolution.
     * @return the confidence score
     */
    public float getConfidence() {
        return confidence;
    }
}
