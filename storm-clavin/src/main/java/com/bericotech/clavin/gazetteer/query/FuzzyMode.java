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
 * FuzzyMode.java
 *
 *###################################################################*/

package com.bericotech.clavin.gazetteer.query;

/**
 * This enum is used to indicate how and when to apply fuzzy matching
 * for gazetteer queries.
 */
public enum FuzzyMode {
    /**
     * Do not use fuzzy matching.
     */
    OFF,
    /**
     * Use fuzzy matching only if no exact matches can be found. This is
     * the original behavior of CLAVIN when fuzzy=true.
     */
    NO_EXACT,
    /**
     * Use fuzzy matching to fill search results, up to the requested
     * maximum number, after all exact matches have been discovered.
     */
    FILL;

    /**
     * Indicates whether a fuzzy query should be performed in this mode based
     * on the number of results found and the maximum number of results.
     * @param maxResults the maximum number of results to return
     * @param exactResults the number of exact results found
     * @return <code>true</code> if fuzzy matching should be applied
     */
    public boolean useFuzzyMatching(final int maxResults, final int exactResults) {
        switch (this) {
            case OFF: return false;
            case NO_EXACT: return exactResults == 0;
            case FILL: return exactResults < maxResults;
            default: throw new IllegalStateException("Unknown FuzzyMode: " + this.name());
        }
    }
}
