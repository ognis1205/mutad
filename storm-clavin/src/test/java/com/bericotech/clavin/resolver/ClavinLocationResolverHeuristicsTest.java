package com.bericotech.clavin.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.extractor.LocationOccurrence;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
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
 * LuceneLocationResolverHeuristicsTest.java
 *
 *###################################################################*/

/**
 * Tests the mapping of location names into
 * {@link ResolvedLocation} objects as performed by
 * {@link ClavinLocationResolver#resolveLocations(List, boolean)}.
 */
public class ClavinLocationResolverHeuristicsTest {
    public final static Logger logger = LoggerFactory.getLogger(ClavinLocationResolverHeuristicsTest.class);

    // expected geonameID numbers for given location names
    private static final int BOSTON_MA = 4930956;
    private static final int HAVERHILL_MA = 4939085;
    private static final int WORCESTER_MA = 4956184;
    private static final int SPRINGFIELD_MA = 4951788;
    private static final int CHICAGO_IL = 4887398;
    private static final int ROCKFORD_IL = 4907959;
    private static final int SPRINGFIELD_IL = 4250542;
    private static final int DECATUR_IL = 4236895;
    private static final int KANSAS_CITY_MO = 4393217;
    private static final int SPRINGFIELD_MO = 4409896;
    private static final int ST_LOUIS_MO = 4407066;
    private static final int INDEPENDENCE_MO = 4391812;
    private static final int LONDON_UK = 2643743;
    private static final int MANCHESTER_UK = 2643123;
    private static final int HAVERHILL_UK = 2647310;
    private static final int TORONTO_ON = 6167865;
    private static final int OTTAWA_ON = 6094817;
    private static final int HAMILTON_ON = 5969782;
    private static final int KITCHENER_ON = 5992996;
    private static final int LONDON_ON = 6058560;
    private static final int CAIRO_EG = 360630;
    private static final int BENGHAZI_LY = 88319;
    private static final int VIRGINIA_US = 6254928;
    private static final int WASHINGTON_DC = 4140963;
    private static final int MARYLAND_US = 4361885;
    private static final int SEATTLE_WA = 5809844;
    private static final int WASHINGTON_STATE_US = 5815135;
    private static final int TACOMA_WA = 5812944;

    private static final int NO_HEURISTICS_MAX_HIT_DEPTH = 1;
    private static final int NO_HEURISTICS_MAX_CONTEXT_WINDOW = 1;
    private static final int HEURISTICS_MAX_HIT_DEPTH = 5;
    private static final int HEURISTICS_MAX_CONTEXT_WINDOW = 5;

    private ClavinLocationResolver resolver;
    private List<ResolvedLocation> resolvedLocations;

    /**
     * Instantiate two {@link ClavinLocationResolver} objects, one without
     * context-based heuristic matching and other with it turned on.
     */
    @Before
    public void setUp() throws ClavinException {
        resolver = new ClavinLocationResolver(new LuceneGazetteer(new File("./IndexDirectory")));
    }

    private List<ResolvedLocation> resolveNoHeuristics(final List<LocationOccurrence> locs, final boolean fuzzy)
            throws ClavinException {
        return resolver.resolveLocations(locs, NO_HEURISTICS_MAX_HIT_DEPTH, NO_HEURISTICS_MAX_CONTEXT_WINDOW, fuzzy);
    }

    private List<ResolvedLocation> resolveWithHeuristics(final List<LocationOccurrence> locs, final boolean fuzzy)
            throws ClavinException {
        return resolver.resolveLocations(locs, HEURISTICS_MAX_HIT_DEPTH, HEURISTICS_MAX_CONTEXT_WINDOW, fuzzy);
    }

    /**
     * Ensure we select the correct {@link ResolvedLocation} objects
     * without using context-based heuristic matching.
     *
     * Without heuristics, {@link ClavinLocationResolver} will default to
     * mapping location name Strings to the matching
     * {@link ResolvedLocation} object with the greatest (sort boosted) population.
     */
    @Test
    public void testNoHeuristics() throws ClavinException {
        String[] locations = {"Haverhill", "Worcester", "Springfield", "Kansas City"};

        resolvedLocations = resolveNoHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), false);

        assertEquals("LocationResolver chose the wrong \"Haverhill\"", HAVERHILL_MA, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Worcester\"", WORCESTER_MA, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_MO, resolvedLocations.get(2).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Kansas City\"", KANSAS_CITY_MO, resolvedLocations.get(3).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct Springfield in a document about
     * Massachusetts using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsMassachusetts() throws ClavinException {
        String[] locations = {"Boston", "Haverhill", "Worcester", "Springfield", "Leominister"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Boston\"", BOSTON_MA, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Haverhill\"", HAVERHILL_MA, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Worcester\"", WORCESTER_MA, resolvedLocations.get(2).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_MA, resolvedLocations.get(3).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct Springfield in a document about
     * Illinois using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsIllinois() throws ClavinException {
        String[] locations = {"Chicago", "Rockford", "Springfield", "Decatur"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Chicago\"", CHICAGO_IL, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Rockford\"", ROCKFORD_IL, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_IL, resolvedLocations.get(2).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Decatur\"", DECATUR_IL, resolvedLocations.get(3).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct Springfield in a document about
     * Missouri using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsMissouri() throws ClavinException {
        String[] locations = {"Kansas City", "Springfield", "St. Louis", "Independence"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Kansas City\"", KANSAS_CITY_MO, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Springfield\"", SPRINGFIELD_MO, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"St. Louis\"", ST_LOUIS_MO, resolvedLocations.get(2).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Independence\"", INDEPENDENCE_MO, resolvedLocations.get(3).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct Haverhill in a document about
     * England using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsEngland() throws ClavinException {
        String[] locations = {"London", "Manchester", "Haverhill"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"London\"", LONDON_UK, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Manchester\"", MANCHESTER_UK, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Haverhill\"", HAVERHILL_UK, resolvedLocations.get(2).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct London in a document about
     * Ontario using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsOntario() throws ClavinException {
        String[] locations = {"Toronto", "Ottawa", "Hamilton", "Kitchener", "London"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Toronto\"", TORONTO_ON, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Ottawa\"", OTTAWA_ON, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Hamilton\"", HAMILTON_ON, resolvedLocations.get(2).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Kitchener\"", KITCHENER_ON, resolvedLocations.get(3).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"London\"", LONDON_ON, resolvedLocations.get(4).getGeoname().getGeonameID());
    }

    /**
     * Tests some border cases involving the resolver.
     */
    @Test
    public void testBorderCases() throws ClavinException {
        // ensure we get no matches for this crazy String
        String[] locations = {"jhadghaoidhg"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), false);
        assertTrue("Heuristic LocationResolver fuzzy off, no match", resolvedLocations.isEmpty());

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);
        assertTrue("Heuristic LocationResolver fuzzy on, no match", resolvedLocations.isEmpty());
    }

    /**
     * Checks fix of bug where admin1 codes from different countries
     * were treated as equal.
     */
    @Test
    public void testHeuristicsNorthAfrica() throws ClavinException {
        String[] locations = {"Cairo", "Benghazi"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Cairo\"", CAIRO_EG, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Benghazi\"", BENGHAZI_LY, resolvedLocations.get(1).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct Washington in a document about
     * Washington, DC using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsDC() throws ClavinException {
        String[] locations = {"Virginia", "Washington", "Maryland"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Virginia\"", VIRGINIA_US, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Washington\"", WASHINGTON_DC, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Maryland\"", MARYLAND_US, resolvedLocations.get(2).getGeoname().getGeonameID());
    }

    /**
     * Ensure we select the correct Washington in a document about
     * Washington State using context-based heuristic matching.
     */
    @Test
    public void testHeuristicsWA() throws ClavinException {
        String[] locations = {"Seattle", "Washington", "Tacoma"};

        resolvedLocations = resolveWithHeuristics(ClavinLocationResolverTest.makeOccurrencesFromNames(locations), true);

        assertEquals("LocationResolver chose the wrong \"Seattle\"", SEATTLE_WA, resolvedLocations.get(0).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Washington\"", WASHINGTON_STATE_US, resolvedLocations.get(1).getGeoname().getGeonameID());
        assertEquals("LocationResolver chose the wrong \"Tacoma\"", TACOMA_WA, resolvedLocations.get(2).getGeoname().getGeonameID());
    }
}