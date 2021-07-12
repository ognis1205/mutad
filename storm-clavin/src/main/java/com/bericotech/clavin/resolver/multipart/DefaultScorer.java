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
 * DefaultScorer.java
 *
 *###################################################################*/


package com.bericotech.clavin.resolver.multipart;

import static com.bericotech.clavin.util.DamerauLevenshtein.damerauLevenshteinDistanceCaseInsensitive;

import com.bericotech.clavin.resolver.multipart.MatchedLocation.Match;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The default scorer combines several weighted metrics to return a score
 * in the range [0.0, 1.0].  The score components are:
 *
 * - Match Ratio (M)
 *   the number of matched terms / the number of input terms
 *
 * - Component Score (C)
 *   A metric boosting the scores of matches containing particular location
 *   types.  From highest to lowest: CITY, COUNTRY, ADMIN1, ADMIN2, ADMIN3,
 *   ADMIN4
 *
 * - Average Damerau-Levenshtein (DL) Distance (D)
 *   The average DL distance (number of edits) between the original search
 *   term and the matched name. This metric is inverted (1/DL) because
 *   lower DL numbers are better. Identical matches are given a score of 1.
 *
 * - Average Search Depth (S)
 *   The average rank within the search results for the parents of the
 *   most specific match. Matches including only a single location will
 *   receive a score of 1.
 *
 * The components are weighted according to the following formula:
 * <code>(0.40 * M) + (0.25 * C) + (0.20 * D) + (0.15 * S)</code>
 */
public class DefaultScorer implements Scorer {
    private static final double MATCH_RATIO_WEIGHT = 0.40d;
    private static final double COMPONENT_SCORE_WEIGHT = 0.25d;
    private static final double DL_DISTANCE_WEIGHT = 0.20d;
    private static final double SEARCH_DEPTH_WEIGHT = 0.15d;

    private static final Map<SearchLevel, Integer> COMPONENT_WEIGHTS;
    private static final Map<SearchLevel, Integer> SINGLE_COMPONENT_WEIGHTS;
    private static final int MAX_COMPONENT_WEIGHT;
    private static final int MAX_SINGLE_COMPONENT_WEIGHT;
    static {
        Map<SearchLevel, Integer> weightMap = new EnumMap<SearchLevel, Integer>(SearchLevel.class);
        weightMap.put(SearchLevel.CITY, 5);
        weightMap.put(SearchLevel.COUNTRY, 4);
        weightMap.put(SearchLevel.ADMIN1, 3);
        weightMap.put(SearchLevel.ADMIN2, 1);
        weightMap.put(SearchLevel.ADMIN3, 1);
        weightMap.put(SearchLevel.ADMIN4, 1);
        weightMap.put(SearchLevel.ADMINX, 1);
        COMPONENT_WEIGHTS = Collections.unmodifiableMap(weightMap);

        int maxWeight = 0;
        for (Integer weight : weightMap.values()) {
            maxWeight += weight;
        }
        MAX_COMPONENT_WEIGHT = maxWeight;

        Map<SearchLevel, Integer> singleWeightMap = new EnumMap<SearchLevel, Integer>(SearchLevel.class);
        singleWeightMap.put(SearchLevel.COUNTRY, 5);
        singleWeightMap.put(SearchLevel.ADMIN1, 4);
        singleWeightMap.put(SearchLevel.CITY, 3);
        singleWeightMap.put(SearchLevel.ADMIN2, 2);
        singleWeightMap.put(SearchLevel.ADMIN3, 1);
        singleWeightMap.put(SearchLevel.ADMIN4, 1);
        singleWeightMap.put(SearchLevel.ADMINX, 1);
        SINGLE_COMPONENT_WEIGHTS = Collections.unmodifiableMap(singleWeightMap);

        int maxSingleWeight = 0;
        for (Integer weight : singleWeightMap.values()) {
            maxSingleWeight += weight;
        }
        MAX_SINGLE_COMPONENT_WEIGHT = maxSingleWeight;
    }

    @Override
    public double score(final List<String> terms, final MatchedLocation candidate) {
        int matchCount = candidate.getMatchCount();

        double totalDL = 0.0d;
        int compWeight = 0;
        double totalDepth = 0.0d;
        for (Match match : candidate.getMatches()) {
            // calculate inverse DL distance
            int dl = damerauLevenshteinDistanceCaseInsensitive(match.getLocation().getLocation().getText(),
                    match.getLocation().getMatchedName());
            totalDL += dl > 0 ? 1.0d / dl : 1.0d;

            // calculate component weight
            Integer weight = matchCount > 1 ? COMPONENT_WEIGHTS.get(match.getLevel()) : SINGLE_COMPONENT_WEIGHTS.get(match.getLevel());
            compWeight += weight != null ? weight : 0;

            // calculate inverse search depth; since depths are 0-indexed, increase all values by 1
            totalDepth += 1.0d / (match.getDepth() + 1);
        }

        double matchRatio = (double) matchCount / terms.size();
        double avgDL = totalDL / matchCount;
        double compScore = (double) compWeight / (matchCount > 1 ? MAX_COMPONENT_WEIGHT : MAX_SINGLE_COMPONENT_WEIGHT);
        double avgDepth = totalDepth / matchCount;

        double score = (MATCH_RATIO_WEIGHT * matchRatio) +
                (DL_DISTANCE_WEIGHT * avgDL) +
                (COMPONENT_SCORE_WEIGHT * compScore) +
                (SEARCH_DEPTH_WEIGHT * avgDepth);
        return score;
    }

    @Override
    public double getMinimumScore() {
        return 0.0d;
    }

    @Override
    public double getMaximumScore() {
        return 1.0d;
    }
}
