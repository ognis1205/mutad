package com.bericotech.clavin.resolver.multipart;



import static org.junit.Assert.*;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.gazetteer.GeoName;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import com.bericotech.clavin.resolver.ResolvedLocation;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
 * MultipartLocationResolverTest.java
 *
 *###################################################################*/

/**
 * Tests the mapping of location names into
 * {@link com.bericotech.clavin.resolver.ResolvedLocation} objects as performed by
 * {@link com.bericotech.clavin.resolver.multipart.MultipartLocationResolver#resolveMultipartLocation(MultipartLocationName, boolean)}.
 */
@RunWith(Parameterized.class)
public class MultipartLocationResolverTest {
    // expected geonameID numbers for given location names
    private static final int UNITED_STATES = 6252001;
        private static final int MASSACHUSETTS = 6254926;
            private static final int BOSTON_MA = 4930956;
            private static final int HAVERHILL_MA = 4939085;
            private static final int WORCESTER_MA = 4956184;
            private static final int SPRINGFIELD_MA = 4951788;
        private static final int MISSOURI = 4398678;
            private static final int SPRINGFIELD_MO = 4409896;
        private static final int ILLINOIS = 4896861;
            private static final int SPRINGFIELD_IL = 4250542;
        private static final int VIRGINIA = 6254928;
            private static final int FAIRFAX_COUNTY_VA = 4758041;
            private static final int RESTON_VA = 4781530;
            private static final int SPRINGFIELD_VA = 4787117;
        private static final int OREGON = 5744337;
            private static final int SPRINGFIELD_OR = 5754005;
        private static final int DELAWARE = 4142224;
            private static final int BETHEL_DE_US = 4141443;
    private static final int GERMANY = 2921044;
        private static final int NR_WESTPHALIA = 2861876; // state of North Rhine-Westphalia
            private static final int BETHEL_GER = 2949766;
    private static final int UNITED_KINGDOM = 2635167;
        private static final int ENGLAND = 6269131;
            private static final int LONDON_UK_41 = 2643741;
            private static final int LONDON_UK_43 = 2643743;
            private static final int HAVERHILL_UK = 2647310;
            private static final int WORCESTER_UK = 2633563;
        private static final int OXFORDSHIRE = 2640726;
            private static final int OXFORD_UK = 2640729;
    private static final int CANADA = 6251999;
        private static final int ONTARIO = 6093943;
            private static final int LONDON_ON = 6058560;
    private static final int PHILIPPINES = 1694008;
        private static final int DAVAO = 7521309;
        private static final int DAVAO_ORIENTAL = 1715342;
            private static final int BOSTON_PH = 1723862;
    private static final int SWITZERLAND = 2658434;
        private static final int ZURICH_CANTON = 2657895;
            private static final int ZURICH_CITY = 2657896;


    @Parameters(name="{index}: multipartResolve({0} {1} {2})")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { "Springfield", "Massachusetts", "United States", new Integer[] { SPRINGFIELD_MA }, MASSACHUSETTS, UNITED_STATES },
            { "Springfield", "Illinois", "United States", new Integer[] { SPRINGFIELD_IL }, ILLINOIS, UNITED_STATES },
            { "Springfield", "Missouri", "United States", new Integer[] { SPRINGFIELD_MO }, MISSOURI, UNITED_STATES },
            { "Springfield", "Virginia", "United States", new Integer[] { SPRINGFIELD_VA }, VIRGINIA, UNITED_STATES },
            { "Springfield", "Oregon", "United States", new Integer[] { SPRINGFIELD_OR }, OREGON, UNITED_STATES },
            { "Reston", "Virginia", "United States", new Integer[] { RESTON_VA }, VIRGINIA, UNITED_STATES },
            { "Reston", "Fairfax County", "United States", new Integer[] { RESTON_VA }, FAIRFAX_COUNTY_VA, UNITED_STATES },
            { "Reston", "Fairfax", "US", new Integer[] { RESTON_VA }, FAIRFAX_COUNTY_VA, UNITED_STATES },
            { "Boston", "Massachusetts", "United States", new Integer[] { BOSTON_MA }, MASSACHUSETTS, UNITED_STATES },
            { "Haverhill", "Massachusetts", "United States", new Integer[] { HAVERHILL_MA }, MASSACHUSETTS, UNITED_STATES },
            { "Worcester", "Massachusetts", "United States", new Integer[] { WORCESTER_MA }, MASSACHUSETTS, UNITED_STATES },
            { "Haverhill", "England", "United Kingdom", new Integer[] { HAVERHILL_UK }, ENGLAND, UNITED_KINGDOM },
            { "Worcester", "England", "United Kingdom", new Integer[] { WORCESTER_UK }, ENGLAND, UNITED_KINGDOM },
            { "Oxford", "England", "United Kingdom", new Integer[] { OXFORD_UK }, ENGLAND, UNITED_KINGDOM },
            { "Oxford", "Oxfordshire", "United Kingdom", new Integer[] { OXFORD_UK }, OXFORDSHIRE, UNITED_KINGDOM },
            { "London", "England", "United Kingdom", new Integer[] { LONDON_UK_43 }, ENGLAND, UNITED_KINGDOM },
            { "London", "Ontario", "Canada", new Integer[] { LONDON_ON }, ONTARIO, CANADA },
            { "Boston", "Davao", "Philippines", new Integer[] { BOSTON_PH }, DAVAO, PHILIPPINES },
            { "Boston", "Davao Oriental", "Philippines", new Integer[] { BOSTON_PH }, DAVAO_ORIENTAL, PHILIPPINES },
            { "Bethel", "Delaware", "United States", new Integer[] { BETHEL_DE_US }, DELAWARE, UNITED_STATES },
            { "Bethel", "North Rhine-Westphalia", "Germany", new Integer[] { BETHEL_GER }, NR_WESTPHALIA, GERMANY },
            { "Bethel", "DE", "US", new Integer[] { BETHEL_DE_US }, DELAWARE, UNITED_STATES },
            { "Bethel", "NRW", "DE", new Integer[] { BETHEL_GER }, NR_WESTPHALIA, GERMANY },
            { "London", "ENG", "UK", new Integer[] { LONDON_UK_43 }, ENGLAND, UNITED_KINGDOM },
            { "London", "ENG", "GB", new Integer[] { LONDON_UK_43 }, ENGLAND, UNITED_KINGDOM },
            { "London", "ON", "CA", new Integer[] { LONDON_ON }, ONTARIO, CANADA },
            { "London", "ON", "CAN", new Integer[] { LONDON_ON }, ONTARIO, CANADA },
            { "Zurich", "ZH", "CH", new Integer[] { ZURICH_CITY }, ZURICH_CANTON, SWITZERLAND },
            { "", "ENG", "UK", null, ENGLAND, UNITED_KINGDOM },
            { "London", "", "UK", new Integer[] { LONDON_UK_43 }, null, UNITED_KINGDOM },
            { "London", "", "", new Integer[] { LONDON_UK_43 }, null, null },
            { "", "ENG", "", null, ENGLAND, null },
            { "", "", "UK", null, null, UNITED_KINGDOM },
            { "", "", "", null, null, null },
            { null, null, null, null, null, null }
        });
    }

    private static MultipartLocationResolver resolver;

    @BeforeClass
    public static void setUpClass() throws ClavinException {
        resolver = new MultipartLocationResolver(new LuceneGazetteer(new File("./IndexDirectory")));
    }

    private final String city;
    private final String state;
    private final String country;
    private final Set<Integer> cityIds;
    private final Integer stateId;
    private final Integer countryId;

    public MultipartLocationResolverTest(String city, String state, String country,
            Integer[] ctyIds, Integer stateId, Integer countryId) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.cityIds = new HashSet<Integer>();
        if (ctyIds != null && ctyIds.length > 0) {
            this.cityIds.addAll(Arrays.asList(ctyIds));
        }
        this.stateId = stateId;
        this.countryId = countryId;
    }

    @Test
    public void testResolveMultipartLocation() throws ClavinException {
        MultipartLocationName mpLoc = new MultipartLocationName(city, state, country);
        ResolvedMultipartLocation rLoc = resolver.resolveMultipartLocation(mpLoc, false);
        verifyCity(rLoc.getCity());
        verifyLocation("state", stateId, rLoc.getState());
        verifyLocation("country", countryId, rLoc.getCountry());
    }

    private void verifyCity(final ResolvedLocation loc) {
        if (cityIds.isEmpty()) {
            assertNull("expected null city", loc);
        } else {
            assertNotNull("expected non-null city", loc);
            GeoName geo = loc.getGeoname();
            assertTrue(String.format("Incorrect city [%s]; expected one of %s", geo, cityIds), cityIds.contains(geo.getGeonameID()));
        }
    }

    private void verifyLocation(final String label, final Integer expected, final ResolvedLocation loc) {
        if (expected == null) {
            assertNull(String.format("expected null %s", label), loc);
        } else {
            assertNotNull(String.format("expected non-null %s", label), loc);
            GeoName geo = loc.getGeoname();
            assertEquals(String.format("Incorrect %s [%s]", label, geo), expected.intValue(), geo.getGeonameID());
        }
    }
}
