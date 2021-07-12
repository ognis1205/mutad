package com.bericotech.clavin.resolver;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.extractor.LocationOccurrence;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * LuceneLocationResolver.java
 *
 *###################################################################*/

/**
 * Resolves location names into GeoName objects.
 *
 * Takes location names extracted from unstructured text documents by
 * {@link com.bericotech.clavin.extractor.LocationExtractor} and resolves them into the appropriate
 * geographic entities (as intended by the document's author based on
 * context) by finding the best match in a gazetteer.
 *
 * @deprecated 2.0.0 Use {@link ClavinLocationResolver}
 */
@Deprecated
public class LuceneLocationResolver implements LocationResolver {
    public final static Logger logger = LoggerFactory.getLogger(LuceneLocationResolver.class);

    private final ClavinLocationResolver delegate;

    // maximum number of matches to be fetched from Lucene index
    // (i.e., search depth) -- use a value of 1 to simply retrieve the
    // matching geo entity having the highest population
    private int maxHitDepth;

    // maximum number of adjacent location name to consider during
    // heuristic matching (i.e., search breadth) -- use a value of 1 to
    // turn off context-based heuristics
    private int maxContextWindow;

    /**
     * Builds a {@link LuceneLocationResolver} by loading a pre-built Lucene
     * index from disk and setting configuration parameters for
     * resolving location names to GeoName objects.
     *
     * @param indexDir              Lucene index directory to be loaded
     * @param maxHitDepth           number of candidate matches to consider
     * @param maxContextWindow      how much context to consider when resolving
     * @throws IOException
     * @throws ParseException
     * @deprecated 2.0.0 Use {@link ClavinLocationResolver}
     */
    @Deprecated
    public LuceneLocationResolver(File indexDir, int maxHitDepth, int maxContextWindow) throws IOException, ParseException {
        logger.warn("LuceneLocationResolver is deprecated.  Use ClavinLocationResolver.");
        try {
            delegate = new ClavinLocationResolver(new LuceneGazetteer(indexDir));
        } catch (ClavinException ce) {
            Throwable t = ce.getCause();
            if (t instanceof ParseException) {
                throw (ParseException)t;
            } else if (t instanceof IOException) {
                throw (IOException)t;
            } else {
                throw new IllegalStateException("Error initializing LuceneLocationResolver.", ce);
            }
        }
    }

    /**
     * Resolves the supplied list of location names into
     * {@link ResolvedLocation}s containing {@link com.bericotech.clavin.gazetteer.GeoName} objects.
     *
     * Calls {@link com.bericotech.clavin.gazetteer.query.Gazetteer#getClosestLocations} on
     * each location name to find all possible matches, then uses
     * heuristics to select the best match for each by calling
     * {@link ClavinLocationResolver#pickBestCandidates}.
     *
     * @param locations         list of location names to be resolved
     * @param fuzzy             switch for turning on/off fuzzy matching
     * @return                  list of {@link ResolvedLocation} objects
     * @throws ParseException
     * @throws IOException
     * @deprecated 2.0.0 Use {@link ClavinLocationResolver#resolveLocations(java.util.List, boolean)} or
     *             {@link ClavinLocationResolver#resolveLocations(java.util.List, int, int, boolean)}
     **/
    @Override
    @Deprecated
    public List<ResolvedLocation> resolveLocations(List<LocationOccurrence> locations, boolean fuzzy) throws IOException, ParseException {
        logger.warn("LuceneLocationResolver is deprecated.  Use ClavinLocationResolver.");
        try {
            return delegate.resolveLocations(locations, maxHitDepth, maxContextWindow, fuzzy);
        } catch (ClavinException ce) {
            Throwable t = ce.getCause();
            if (t instanceof ParseException) {
                throw (ParseException)t;
            } else if (t instanceof IOException) {
                throw (IOException)t;
            } else {
                throw new IllegalStateException("Error resolving locations.", ce);
            }
        }
    }
}
