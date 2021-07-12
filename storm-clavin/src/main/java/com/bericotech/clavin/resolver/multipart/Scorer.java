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
 * Scorer.java
 *
 *###################################################################*/

package com.bericotech.clavin.resolver.multipart;

import java.util.List;

/**
 * Evaluates a particular MatchedLocation against the provided search
 * terms and generates a score used to select the best location for
 * a multi-part search.
 */
public interface Scorer {
    /**
     * Generate a score representing how well the candidate result
     * matches the search terms.  Higher scores indicate better
     * matches.
     * @param terms the original search terms
     * @param candidate the candidate match
     * @return a numeric score indicating the quality of this candidate,
     *         higher scores are better
     */
    double score(final List<String> terms, final MatchedLocation candidate);

    /**
     * Get the minimum score returned by this scorer.
     * @return the minimum score for a candidate
     */
    double getMinimumScore();

    /**
     * Get the maximum score returned by this scorer.
     * @return the maximum score for a candidate
     */
    double getMaximumScore();
}
