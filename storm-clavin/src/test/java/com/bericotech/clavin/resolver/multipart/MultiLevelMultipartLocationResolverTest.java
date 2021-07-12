package com.bericotech.clavin.resolver.multipart;



import static org.junit.Assert.*;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.gazetteer.GeoName;
import com.bericotech.clavin.gazetteer.query.LuceneGazetteer;
import com.bericotech.clavin.resolver.ResolvedLocation;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
 * MultiLevelMultipartLocationResolverTest.java
 *
 *###################################################################*/

/**
 * Tests mapping of city and N-level administrative divisions to a
 * single location.
 */
@RunWith(Parameterized.class)
public class MultiLevelMultipartLocationResolverTest {
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
            private static final int FAIRFAX_VA = 4758023;
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
    private static final int AUSTRALIA = 2077456;
        private static final int ASHMORE_AND_CARTIER_ISLANDS = 2077507;
    private static final int NETHERLANDS_ANTILLES = 8505032;
    private static final int CLIPPERTON_ISLAND = 4020092;


    @Parameters(name="{index}: multipartResolve({0} {1})")
    public static Iterable<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { Arrays.asList("Springfield", "Massachusetts", "United States"), SPRINGFIELD_MA },
            { Arrays.asList("Springfield", "Illinois", "United States"), SPRINGFIELD_IL },
            { Arrays.asList("Springfield", "Missouri", "United States"), SPRINGFIELD_MO },
            { Arrays.asList("Springfield", "Virginia", "United States"), SPRINGFIELD_VA },
            { Arrays.asList("Springfield", "Oregon", "United States"), SPRINGFIELD_OR },
            { Arrays.asList("Reston", "Virginia", "United States"), RESTON_VA },
            { Arrays.asList("Reston", "Fairfax", "Virginia", "United States"), RESTON_VA },
            { Arrays.asList("Reston", "Fairfax County", "VA", "US"), RESTON_VA },
            { Arrays.asList("Reston", "Fairfax"), RESTON_VA },
            { Arrays.asList("Fairfax", "Virginia"), FAIRFAX_VA },
            { Arrays.asList("Fairfax County", "VA"), FAIRFAX_COUNTY_VA },
            { Arrays.asList("Reston", "Virginia", "CA"), null },
            { Arrays.asList("Boston", "Massachusetts", "United States"), BOSTON_MA },
            { Arrays.asList("Haverhill", "Massachusetts", "United States"), HAVERHILL_MA },
            { Arrays.asList("Worcester", "Massachusetts", "United States"), WORCESTER_MA },
            { Arrays.asList("Haverhill", "England", "United Kingdom"), HAVERHILL_UK },
            { Arrays.asList("Worcester", "England", "United Kingdom"), WORCESTER_UK },
            { Arrays.asList("Oxford", "England", "United Kingdom"), OXFORD_UK },
            { Arrays.asList("Oxford", "Oxfordshire", "United Kingdom"), OXFORD_UK },
            { Arrays.asList("London", "England", "United Kingdom"), LONDON_UK_43 },
            { Arrays.asList("London", "Ontario", "Canada"), LONDON_ON },
            { Arrays.asList("Boston", "Davao", "Philippines"), BOSTON_PH },
            { Arrays.asList("Boston", "Davao Oriental", "Philippines"), BOSTON_PH },
            { Arrays.asList("Bethel", "Delaware", "United States"), BETHEL_DE_US },
            { Arrays.asList("Bethel", "North Rhine-Westphalia", "Germany"), BETHEL_GER },
            { Arrays.asList("Bethel", "DE", "US"), BETHEL_DE_US },
            { Arrays.asList("Bethel", "NRW", "DE"), BETHEL_GER },
            { Arrays.asList("London", "ENG", "UK"), LONDON_UK_43 },
            { Arrays.asList("London", "ENG", "GB"), LONDON_UK_43 },
            { Arrays.asList("London", "ON", "CA"), LONDON_ON },
            { Arrays.asList("London", "ON", "CAN"), LONDON_ON },
            { Arrays.asList("Zurich", "ZH", "CH"), ZURICH_CITY },
            { Arrays.asList("", "ENG", "UK"), ENGLAND },
            { Arrays.asList("London", "", "UK"), LONDON_UK_43 },
            { Arrays.asList("London", "", ""), LONDON_UK_43 },
            { Arrays.asList("", "ENG", ""), ENGLAND },
            { Arrays.asList("", "", "UK"), UNITED_KINGDOM },
            { Arrays.asList("Ashmore and Cartier Islands"), ASHMORE_AND_CARTIER_ISLANDS },
            { Arrays.asList("Ashmore and Cartier Islands", "Australia"), ASHMORE_AND_CARTIER_ISLANDS },
            { Arrays.asList("Netherlands Antilles"), NETHERLANDS_ANTILLES },
            { Arrays.asList("Parish of Saint Thomas", "Ashmore and Cartier Islands"), null},
            { Arrays.asList("Clipperton Island"), CLIPPERTON_ISLAND },
            { Arrays.asList("Clipperton Island", "France"), CLIPPERTON_ISLAND },
            { Arrays.asList("", "", ""), null },
            { Collections.EMPTY_LIST, null }
        });
    }

    private static MultipartLocationResolver resolver;

    @BeforeClass
    public static void setUpClass() throws ClavinException {
        resolver = new MultipartLocationResolver(new LuceneGazetteer(new File("./IndexDirectory")));
    }

    private final String[] parts;
    private final Integer expectedId;

    public MultiLevelMultipartLocationResolverTest(List<String> parts, Integer expectedId) {
        this.parts = parts.toArray(new String[0]);
        this.expectedId = expectedId;
    }

    @Test
    public void testResolveMultipartLocation() throws ClavinException {
        ResolvedLocation loc = resolver.resolveLocation(false, parts);
        if (expectedId == null) {
            assertNull("expected null location", loc);
        } else {
            assertNotNull("expected non-null location", loc);
            GeoName geo = loc.getGeoname();
            assertEquals(String.format("Incorrect Location [%s]", geo), expectedId.intValue(), geo.getGeonameID());
        }
    }
}
