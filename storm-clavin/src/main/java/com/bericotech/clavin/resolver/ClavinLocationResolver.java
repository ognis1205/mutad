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
 * ClavinLocationResolver.java
 *
 *###################################################################*/

package com.bericotech.clavin.resolver;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.extractor.LocationOccurrence;
import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.gazetteer.query.AncestryMode;
import com.bericotech.clavin.gazetteer.query.FuzzyMode;
import com.bericotech.clavin.gazetteer.query.Gazetteer;
import com.bericotech.clavin.gazetteer.query.QueryBuilder;
import com.bericotech.clavin.util.ListUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves location names into GeoName objects.
 *
 * Takes location names extracted from unstructured text documents by
 * {@link com.bericotech.clavin.extractor.LocationExtractor} and resolves them into the appropriate
 * geographic entities (as intended by the document's author based on
 * context) by finding the best match in a gazetteer.
 */
public class ClavinLocationResolver {
    /**
     * The default number of candidate matches to consider.
     */
    public static final int DEFAULT_MAX_HIT_DEPTH = 5;

    /**
     * The default context window to consider when resolving matches.
     */
    public static final int DEFAULT_MAX_CONTEXT_WINDOW = 5;

    /**
     * The default ancestry loading mode.
     */
    public static final AncestryMode DEFAULT_ANCESTRY_MODE = AncestryMode.LAZY;

    /**
     * The Gazetteer.
     */
    private final Gazetteer gazetteer;

    /**
     * Set of demonyms to filter out from extracted location names.
     */
    private static HashSet<String> demonyms;

    /**
     * Create a new ClavinLocationResolver.
     * @param gazetteer the Gazetteer to query
     */
    public ClavinLocationResolver(final Gazetteer gazetteer) {
        this.gazetteer = gazetteer;
    }

    /**
     * Get the Gazetteer used by this resolver.
     * @return the configured gazetteer
     */
    public Gazetteer getGazetteer() {
        return gazetteer;
    }

    /**
     * Resolves the supplied list of location names into
     * {@link ResolvedLocation}s containing {@link com.bericotech.clavin.gazetteer.GeoName} objects
     * using the defaults for maxHitDepth and maxContentWindow.
     *
     * Calls {@link Gazetteer#getClosestLocations} on
     * each location name to find all possible matches, then uses
     * heuristics to select the best match for each by calling
     * {@link ClavinLocationResolver#pickBestCandidates}.
     *
     * @param locations          list of location names to be resolved
     * @param fuzzy              switch for turning on/off fuzzy matching
     * @return                   list of {@link ResolvedLocation} objects
     * @throws ClavinException   if an error occurs parsing the search terms
     **/
    public List<ResolvedLocation> resolveLocations(final List<LocationOccurrence> locations, final boolean fuzzy)
            throws ClavinException {
        return resolveLocations(locations, DEFAULT_MAX_HIT_DEPTH, DEFAULT_MAX_CONTEXT_WINDOW, fuzzy, DEFAULT_ANCESTRY_MODE);
    }

    /**
     * Resolves the supplied list of location names into
     * {@link ResolvedLocation}s containing {@link com.bericotech.clavin.gazetteer.GeoName} objects.
     *
     * Calls {@link Gazetteer#getClosestLocations} on
     * each location name to find all possible matches, then uses
     * heuristics to select the best match for each by calling
     * {@link ClavinLocationResolver#pickBestCandidates}.
     *
     * @param locations          list of location names to be resolved
     * @param maxHitDepth        number of candidate matches to consider
     * @param maxContextWindow   how much context to consider when resolving
     * @param fuzzy              switch for turning on/off fuzzy matching
     * @return                   list of {@link ResolvedLocation} objects
     * @throws ClavinException   if an error occurs parsing the search terms
     **/
    @SuppressWarnings("unchecked")
    public List<ResolvedLocation> resolveLocations(final List<LocationOccurrence> locations, final int maxHitDepth,
           final int maxContextWindow, final boolean fuzzy) throws ClavinException {
        return resolveLocations(locations, maxHitDepth, maxContextWindow, fuzzy, DEFAULT_ANCESTRY_MODE);
    }

    /**
     * Resolves the supplied list of location names into
     * {@link ResolvedLocation}s containing {@link com.bericotech.clavin.gazetteer.GeoName} objects.
     *
     * Calls {@link Gazetteer#getClosestLocations} on
     * each location name to find all possible matches, then uses
     * heuristics to select the best match for each by calling
     * {@link ClavinLocationResolver#pickBestCandidates}.
     *
     * @param locations          list of location names to be resolved
     * @param maxHitDepth        number of candidate matches to consider
     * @param maxContextWindow   how much context to consider when resolving
     * @param fuzzy              switch for turning on/off fuzzy matching
     * @param ancestryMode       the ancestry loading mode
     * @return                   list of {@link ResolvedLocation} objects
     * @throws ClavinException   if an error occurs parsing the search terms
     **/
    @SuppressWarnings("unchecked")
    public List<ResolvedLocation> resolveLocations(final List<LocationOccurrence> locations, final int maxHitDepth,
            final int maxContextWindow, final boolean fuzzy, final AncestryMode ancestryMode) throws ClavinException {
        // are you forgetting something? -- short-circuit if no locations were provided
        if (locations == null || locations.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        /* Various named entity recognizers tend to mistakenly extract demonyms
         * (i.e., names for residents of localities (e.g., American, British))
         * as place names, which tends to gum up the works, so we make sure to
         * filter them out from the list of {@link LocationOccurrence}s passed
         * to the resolver.
         */
        List<LocationOccurrence> filteredLocations = new ArrayList<LocationOccurrence>();
        for (LocationOccurrence location : locations)
            if (!isDemonym(location))
                filteredLocations.add(location);

        // did we filter *everything* out?
        if (filteredLocations.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        QueryBuilder builder = new QueryBuilder()
                .maxResults(maxHitDepth)
                // translate CLAVIN 1.x 'fuzzy' parameter into NO_EXACT or OFF; it isn't
                // necessary, or desirable to support FILL for the CLAVIN resolution algorithm
                .fuzzyMode(fuzzy ? FuzzyMode.NO_EXACT : FuzzyMode.OFF)
                .ancestryMode(ancestryMode)
                .includeHistorical(true);

        if (maxHitDepth > 1) { // perform context-based heuristic matching
            // stores all possible matches for each location name
            List<List<ResolvedLocation>> allCandidates = new ArrayList<List<ResolvedLocation>>();

            // loop through all the location names
            for (LocationOccurrence location : filteredLocations) {
                // get all possible matches
                List<ResolvedLocation> candidates = gazetteer.getClosestLocations(builder.location(location).build());

                // if we found some possible matches, save them
                if (candidates.size() > 0) {
                    allCandidates.add(candidates);
                }
            }

            // initialize return object
            List<ResolvedLocation> bestCandidates = new ArrayList<ResolvedLocation>();

            // split-up allCandidates into reasonably-sized chunks to
            // limit computational load when heuristically selecting
            // the best matches
            for (List<List<ResolvedLocation>> theseCandidates : ListUtils.chunkifyList(allCandidates, maxContextWindow)) {
                // select the best match for each location name based
                // based on heuristics
                bestCandidates.addAll(pickBestCandidates(theseCandidates));
            }

            return bestCandidates;
        } else { // use no heuristics, simply choose matching location with greatest population
            // initialize return object
            List<ResolvedLocation> resolvedLocations = new ArrayList<ResolvedLocation>();

            // stores possible matches for each location name
            List<ResolvedLocation> candidateLocations;

            // loop through all the location names
            for (LocationOccurrence location : filteredLocations) {
                // choose the top-sorted candidate for each individual
                // location name
                candidateLocations = gazetteer.getClosestLocations(builder.location(location).build());

                // if a match was found, add it to the return list
                if (candidateLocations.size() > 0) {
                    resolvedLocations.add(candidateLocations.get(0));
                }
            }

            return resolvedLocations;
        }
    }

    /**
     * Uses heuristics to select the best match for each location name
     * extracted from a document, choosing from among a list of lists
     * of candidate matches.
     *
     * Although not guaranteeing an optimal solution (enumerating &
     * evaluating each possible combination is too costly), it does a
     * decent job of cracking the "Springfield Problem" by selecting
     * candidates that would make sense to appear together based on
     * common country and admin1 codes (i.e., states or provinces).
     *
     * For example, if we also see "Boston" mentioned in a document
     * that contains "Springfield," we'd use this as a clue that we
     * ought to choose Springfield, MA over Springfield, IL or
     * Springfield, MO.
     *
     * TODO: consider lat/lon distance in addition to shared
     *       CountryCodes and Admin1Codes.
     *
     * @param allCandidates list of lists of candidate matches for locations names
     * @return              list of best matches for each location name
     */
    private List<ResolvedLocation> pickBestCandidates(final List<List<ResolvedLocation>> allCandidates) {
        // initialize return object
        List<ResolvedLocation> bestCandidates = new ArrayList<ResolvedLocation>();

        // variables used in heuristic matching
        Set<CountryCode> countries;
        Set<String> states;
        float score;

        // initial values for variables controlling recursion
        float newMaxScore = 0;
        float oldMaxScore;

        // controls window of Lucene hits for each location considered
        // context-based heuristic matching, initialized as a "magic
        // number" of *3* based on tests of the "Springfield Problem"
        int candidateDepth = 3;

        // keep searching deeper & deeper for better combinations of
        // candidate matches, as long as the scores are improving
        do {
            // reset the threshold for recursion
            oldMaxScore = newMaxScore;

            // loop through all combinations up to the specified depth.
            // first recursive call for each depth starts at index 0
            for (List<ResolvedLocation> combo : generateAllCombos(allCandidates, 0, candidateDepth)) {
                // these lists store the country codes & admin1 codes for each candidate
                countries = EnumSet.noneOf(CountryCode.class);
                states = new HashSet<String>();
                for (ResolvedLocation location: combo) {
                    countries.add(location.getGeoname().getPrimaryCountryCode());
                    states.add(location.getGeoname().getPrimaryCountryCode() + location.getGeoname().getAdmin1Code());
                }

                // calculate a score for this particular combination based on commonality
                // of country codes & admin1 codes, and the cost of searching this deep
                // TODO: tune this score calculation!
                score = ((float)allCandidates.size() / (countries.size() + states.size())) / candidateDepth;

                /* ***********************************************************
                 * "So, at last we meet for the first time for the last time."
                 *
                 * The fact that you're interested enough in CLAVIN to be
                 * reading this means we're interested in talking with you.
                 *
                 * Are you looking for a job, or are you in need of a
                 * customized solution built around CLAVIN?
                 *
                 * Drop us a line at clavin@bericotechnologies.com
                 *
                 * "What's the matter, Colonel Sandurz? CHICKEN?"
                 * **********************************************************/

                // if this is the best we've seen during this loop, update the return value
                if (score > newMaxScore) {
                    newMaxScore = score;
                    bestCandidates = combo;
                }
            }

            // search one level deeper in the next loop
            candidateDepth++;

        } while (newMaxScore > oldMaxScore);
        // keep searching while the scores are monotonically increasing

        return bestCandidates;
    }

    /**
     * Recursive helper function for
     * {@link #pickBestCandidates}.
     *
     * Generates all combinations of candidate matches from each
     * location, down to the specified depth through the lists.
     *
     * Adapted from:
     * http://www.daniweb.com/software-development/java/threads/177956/generating-all-possible-combinations-from-list-of-sublists#post882553
     *
     * @param allCandidates list of lists of candidate matches for all location names
     * @param index         keeps track of which location we're working on for recursive calls
     * @param depth         max depth into list we're searching during this recursion
     * @return              all combinations of candidate matches for each location, down to the specified depth
     */
    private List<List<ResolvedLocation>> generateAllCombos(final List<List<ResolvedLocation>> allCandidates,
            final int index, final int depth) {
        // stopping condition
        if (index == allCandidates.size()) {
            // return a list with an empty list
            List<List<ResolvedLocation>> result = new ArrayList<List<ResolvedLocation>>();
            result.add(new ArrayList<ResolvedLocation>());
            return result;
        }

        // initialize return object
        List<List<ResolvedLocation>> result = new ArrayList<List<ResolvedLocation>>();

        // recursive call
        List<List<ResolvedLocation>> recursive = generateAllCombos(allCandidates, index+1, depth);

        // for each element of the first list of input, up to depth or list size
        for (int j = 0; j < Math.min(allCandidates.get(index).size(), depth); j++) {
            // add the element to all combinations obtained for the rest of the lists
            for (List<ResolvedLocation> recList : recursive) {
                List<ResolvedLocation> newList = new ArrayList<ResolvedLocation>();
                // add element of the first list
                newList.add(allCandidates.get(index).get(j));
                // copy a combination from recursive
                for (ResolvedLocation listItem : recList) {
                    newList.add(listItem);
                }
                // add new combination to result
                result.add(newList);
            }
        }

        return result;
    }

    /**
     * Various named entity recognizers tend to mistakenly extract
     * demonyms (i.e., names for residents of localities (e.g.,
     * American, British)) as place names, which tends to gum up the
     * works for the resolver, so this method filters them out from
     * the list of {@link LocationOccurrence}s passed to the resolver.
     *
     * @param extractedLocation extraction location name to filter
     * @return                  true if input is a demonym, false otherwise
     */
    public static boolean isDemonym(LocationOccurrence extractedLocation) {
        // lazy load set of demonyms
        if (demonyms == null) {
            // populate set of demonyms to filter out from results, source:
            // http://en.wikipedia.org/wiki/List_of_adjectival_and_demonymic_forms_for_countries_and_nations
            demonyms = new HashSet<String>();

            BufferedReader br = new BufferedReader(new InputStreamReader(ClavinLocationResolver.class.getClassLoader().getResourceAsStream("Demonyms.txt")));

            String line;
            try {
                while ((line = br.readLine()) != null)
                    demonyms.add(line);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return demonyms.contains(extractedLocation.getText());
    }
}
