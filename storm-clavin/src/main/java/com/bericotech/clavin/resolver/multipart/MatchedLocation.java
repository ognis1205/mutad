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
 * MatchedLocation.java
 *
 *###################################################################*/

package com.bericotech.clavin.resolver.multipart;

import com.bericotech.clavin.gazetteer.GeoName;
import com.bericotech.clavin.resolver.ResolvedLocation;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A candidate match for a multi-level search.
 */
public class MatchedLocation {
    private static final Logger LOG = LoggerFactory.getLogger(MatchedLocation.class);

    private final Map<SearchLevel, Match> matches;

    public MatchedLocation(final Deque<SearchResult> results) {
        matches = new EnumMap<SearchLevel, Match>(SearchLevel.class);

        Map<SearchLevel, List<ResolvedLocation>> resultsMap = new EnumMap<SearchLevel, List<ResolvedLocation>>(SearchLevel.class);
        for (SearchResult result : results) {
            resultsMap.put(result.level, result.locations);
        }

        ResolvedLocation bestMatch = results.peek().getBestLocation();
        matches.put(results.peek().level, new Match(results.peek().level, bestMatch, 0));
        // if the geoname's ancestry is fully resolved, find the
        // matched ancestors in the search results and populate the map;
        // otherwise, we cannot populate the map with anything other than
        // the best result because we cannot verify which search result
        // is the parent of the selected location
        if (bestMatch.getGeoname().isAncestryResolved()) {
            GeoName parent = bestMatch.getGeoname().getParent();
            while (parent != null) {
                SearchLevel level = SearchLevel.forGeoName(parent);
                if (resultsMap.containsKey(level)) {
                    // find parent GeoName in the results; this should exist because
                    // searches are filtered by ancestry from prior results
                    ResolvedLocation parentLoc = null;
                    List<ResolvedLocation> searchResults = resultsMap.get(level);
                    int depth;
                    for (depth = 0; depth < searchResults.size(); depth++) {
                        ResolvedLocation loc = searchResults.get(depth);
                        if (parent.getGeonameID() == loc.getGeoname().getGeonameID()) {
                            parentLoc = loc;
                            break;
                        }
                    }
                    if (parentLoc == null) {
                        // log this as an error condition; it indicates a problem with either
                        // gazetteer construction or ancestry indexing; this has been noticed
                        // with certain historical locations, specifically Netherlands Antilles (PCLH),
                        // which lists Curacao (PCLIX), another country, as a parent. We shouldn't
                        // fail the entire matching algorithm at this point; just log and ignore
                        LOG.error(String.format("Missing parent [%s] in search results for match: %s.", parent, bestMatch));
                    } else {
                        // found a match, add it to the list
                        matches.put(level, new Match(level, parentLoc, depth));
                    }
                }
                parent = parent.getParent();
            }
        }
    }

    public Match getMatch(final SearchLevel level) {
        return matches.get(level);
    }

    public Match getMostSpecificMatch() {
        Match match = null;
        for (SearchLevel level = SearchLevel.CITY; match == null && level != null; level = level.broaden()) {
            match = matches.get(level);
        }
        return match;
    }

    public Collection<Match> getMatches() {
        return Collections.unmodifiableCollection(matches.values());
    }

    public int getMatchCount() {
        return matches.size();
    }

    public boolean isFullySpecified() {
        return getMatchCount() == SearchLevel.values().length;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (SearchLevel level : SearchLevel.values()) {
            hash = 89 * hash + (matches.containsKey(level) ? matches.get(level).hashCode() : 0);
        }
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
        final MatchedLocation other = (MatchedLocation) obj;
        for (SearchLevel level : SearchLevel.values()) {
            Match mine = getMatch(level);
            Match theirs = other.getMatch(level);
            if (mine != theirs && (mine == null || !mine.equals(theirs))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Match: { ");
        for (SearchLevel level : SearchLevel.values()) {
            builder.append(level).append(": ");
            Match match = matches.get(level);
            if (match != null) {
                builder.append(String.format("[%d] %s (d:%d)", match.getLocation().getGeoname().getGeonameID(),
                        match.getLocation().getGeoname().getName(), match.getDepth()));
            } else {
                builder.append("NULL");
            }
            if (level.canNarrow()) {
                builder.append(", ");
            }
        }
        return builder.append(" }").toString();
    }

    public static class Match {
        private final SearchLevel level;
        private final ResolvedLocation location;
        private final int depth;

        public Match(final SearchLevel level, final ResolvedLocation location, final int depth) {
            this.level = level;
            this.location = location;
            this.depth = depth;
        }

        public SearchLevel getLevel() {
            return level;
        }

        public ResolvedLocation getLocation() {
            return location;
        }

        public int getDepth() {
            return depth;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 43 * hash + (this.level != null ? this.level.hashCode() : 0);
            hash = 43 * hash + (this.location != null ? this.location.hashCode() : 0);
            hash = 43 * hash + this.depth;
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
            final Match other = (Match) obj;
            if (this.level != other.level) {
                return false;
            }
            if (this.location != other.location && (this.location == null || !this.location.equals(other.location))) {
                return false;
            }
            if (this.depth != other.depth) {
                return false;
            }
            return true;
        }
    }
}
