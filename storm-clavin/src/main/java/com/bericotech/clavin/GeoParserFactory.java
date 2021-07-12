package com.bericotech.clavin;

import com.bericotech.clavin.extractor.ApacheExtractor;
import com.bericotech.clavin.extractor.LocationExtractor;
import com.bericotech.clavin.gazetteer.query.Gazetteer;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import java.io.File;
import java.io.IOException;

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
 * GeoParserFactory.java
 *
 *###################################################################*/

/**
 * Simple Factory for the creation of GeoParser instances.  The 'default'
 * instance being Apache OpenNLP as the Extractor and a local Lucene index
 * as the LocationResolver.
 */
public class GeoParserFactory {

    /**
     * Get the default GeoParser, with maxHitDepth and maxContentWindow
     * both set to 1, and fuzzy matching turned off.
     *
     * @param pathToLuceneIndex     Path to the local Lucene index.
     * @return                      GeoParser
     * @throws ClavinException      If the index cannot be created.
     */
    public static GeoParser getDefault(String pathToLuceneIndex) throws ClavinException {
        return getDefault(pathToLuceneIndex, 1, 1, false);
    }

    /**
     * Get a GeoParser with fuzzy matching explicitly turned on or off.
     *
     * @param pathToLuceneIndex     Path to the local Lucene index.
     * @param fuzzy                 Should fuzzy matching be used?
     * @return                      GeoParser
     * @throws ClavinException      If the index cannot be created.
     */
    public static GeoParser getDefault(String pathToLuceneIndex, boolean fuzzy) throws ClavinException {
        return getDefault(pathToLuceneIndex, 1, 1, fuzzy);
    }

    /**
     * Get a GeoParser with defined values for maxHitDepth and
     * maxContentWindow.
     *
     * @param pathToLuceneIndex     Path to the local Lucene index.
     * @param maxHitDepth           Number of candidate matches to consider
     * @param maxContentWindow      How much context to consider when resolving
     * @return                      GeoParser
     * @throws ClavinException      If the index cannot be created.
     */
    public static GeoParser getDefault(String pathToLuceneIndex, int maxHitDepth, int maxContentWindow) throws ClavinException {
        return getDefault(pathToLuceneIndex, maxHitDepth, maxContentWindow, false);
    }

    /**
     * Get a GeoParser with defined values for maxHitDepth and
     * maxContentWindow, and fuzzy matching explicitly turned on or off.
     *
     * @param pathToLuceneIndex     Path to the local Lucene index.
     * @param maxHitDepth           Number of candidate matches to consider
     * @param maxContentWindow      How much context to consider when resolving
     * @param fuzzy                 Should fuzzy matching be used?
     * @return                      GeoParser
     * @throws ClavinException      If the index cannot be created.
     */
    public static GeoParser getDefault(String pathToLuceneIndex, int maxHitDepth, int maxContentWindow, boolean fuzzy)
                    throws ClavinException {
        try {
            // instantiate default LocationExtractor
            LocationExtractor extractor = new ApacheExtractor();
            return getDefault(pathToLuceneIndex, extractor, maxHitDepth, maxContentWindow, fuzzy);
        } catch (IOException ioe) {
            throw new ClavinException("Error creating ApacheExtractor", ioe);
        }
    }

    /**
     * Get a GeoParser with defined values for maxHitDepth and
     * maxContentWindow, fuzzy matching explicitly turned on or off,
     * and a specific LocationExtractor to use.
     *
     * @param pathToLuceneIndex     Path to the local Lucene index.
     * @param extractor             A specific implementation of LocationExtractor to be used
     * @param maxHitDepth           Number of candidate matches to consider
     * @param maxContentWindow      How much context to consider when resolving
     * @param fuzzy                 Should fuzzy matching be used?
     * @return                      GeoParser
     * @throws ClavinException      If the index cannot be created.
     */
    public static GeoParser getDefault(String pathToLuceneIndex, LocationExtractor extractor, int maxHitDepth,
            int maxContentWindow, boolean fuzzy) throws ClavinException {
        // instantiate new LuceneGazetteer
        Gazetteer gazetteer = new LuceneGazetteer(new File(pathToLuceneIndex));
        return new GeoParser(extractor, gazetteer, maxHitDepth, maxContentWindow, fuzzy);
    }
}
