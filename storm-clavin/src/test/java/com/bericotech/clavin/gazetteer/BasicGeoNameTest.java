package com.bericotech.clavin.gazetteer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import org.junit.Before;
import org.junit.Test;

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
 * BasicGeoNameTest.java
 *
 *###################################################################*/

/**
 * Tests to make sure GeoNames gazetteer records are properly parsed
 * into corresponding {@link BasicGeoName} objects.
 *
 */
public class BasicGeoNameTest {
    private static class GeoRecord {
        public final String gazetteerRecord;
        public final BasicGeoName geoName;

        public GeoRecord(final String gaz, final BasicGeoName geo) {
            this.gazetteerRecord = gaz;
            this.geoName = geo;
        }
    }

    private GeoRecord reston;
    private GeoRecord howzEHaji;
    private GeoRecord strabenhaus;
    private GeoRecord noMansLand;
    private GeoRecord chihuahuaDesert;
    private GeoRecord rasSalim;
    private GeoRecord murrayCanyon;
    private GeoRecord boston;
    private GeoRecord gunBarrelCity;
    private GeoRecord unitedStates;
    private GeoRecord fairfaxCounty;
    private GeoRecord virginia;
    private GeoRecord antarctica;
    private GeoRecord coralSeaIslands;
    private GeoRecord campoParish;
    private GeoRecord americanSamoa;
    private GeoRecord australia;

    @Before
    public void setUp() throws IOException {
        // load GeoNames.org sample data file & instantiate corresponding GeoName objects
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File("./src/test/resources/gazetteers/GeoNamesSampleSet.txt")), "UTF-8"));
        String line;
        List<GeoRecord> geoRecords = new ArrayList<GeoRecord>();
        while ((line = r.readLine()) != null) {
            geoRecords.add(new GeoRecord(line, (BasicGeoName) BasicGeoName.parseFromGeoNamesRecord(line)));
        }
        r.close();

        reston = geoRecords.get(0);
        howzEHaji = geoRecords.get(1);
        strabenhaus = geoRecords.get(2);
        noMansLand = geoRecords.get(3);
        chihuahuaDesert = geoRecords.get(4);
        rasSalim = geoRecords.get(5);
        murrayCanyon = geoRecords.get(6);
        boston = geoRecords.get(7);
        gunBarrelCity = geoRecords.get(8);
        unitedStates = geoRecords.get(9);
        fairfaxCounty = geoRecords.get(10);
        virginia = geoRecords.get(11);
        antarctica = geoRecords.get(12);
        coralSeaIslands = geoRecords.get(13);
        campoParish = geoRecords.get(14);
        americanSamoa = geoRecords.get(15);
        australia = geoRecords.get(16);
    }

    /**
     * Test all attributes for a standard US city.
     * @throws ParseException if an error occurs parsing the test date
     */
    @Test
    public void testAllAttributes() throws ParseException {
        GeoName restonGeo = reston.geoName;
        assertEquals("incorrect geonameID", 4781530, restonGeo.getGeonameID());
        assertEquals("incorrect name", "Reston", restonGeo.getName());
        assertEquals("incorrect asciiName", "Reston", restonGeo.getAsciiName());
        assertEquals("incorrect alternateNames", Arrays.asList("Reston","Рестон"), restonGeo.getAlternateNames());
        assertEquals("incorrect latitude", 38.96872, restonGeo.getLatitude(), 0.1);
        assertEquals("incorrect longitude", -77.3411, restonGeo.getLongitude(), 0.1);
        assertEquals("incorrect featureClass", FeatureClass.P, restonGeo.getFeatureClass());
        assertEquals("incorrect featureCode", FeatureCode.PPL, restonGeo.getFeatureCode());
        assertEquals("incorrect primaryCountryCode", CountryCode.US, restonGeo.getPrimaryCountryCode());
        assertEquals("incorrect primaryCountryName", CountryCode.US.name, restonGeo.getPrimaryCountryName());
        assertEquals("incorrect alternateCountryCodes", new ArrayList<CountryCode>(), restonGeo.getAlternateCountryCodes());
        assertEquals("incorrect adminCode1", "VA", restonGeo.getAdmin1Code());
        assertEquals("incorrect adminCode2", "059", restonGeo.getAdmin2Code());
        assertEquals("incorrect adminCode3", "", restonGeo.getAdmin3Code());
        assertEquals("incorrect adminCode4", "", restonGeo.getAdmin4Code());
        assertEquals("incorrect population", 58404, restonGeo.getPopulation());
        assertEquals("incorrect elevation", 100, restonGeo.getElevation());
        assertEquals("incorrect digitalElevationModel", 102, restonGeo.getDigitalElevationModel());
        assertEquals("incorrect timezone", TimeZone.getTimeZone("America/New_York"), restonGeo.getTimezone());
        assertEquals("incorrect modificationDate", new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-14"), restonGeo.getModificationDate());
        assertEquals("incorrect gazetteer record", reston.gazetteerRecord, restonGeo.getGazetteerRecord());
        assertEquals("incorrect parent ancestry key", "US.VA.059", restonGeo.getParentAncestryKey());
        assertNull("ancestry key should be null", restonGeo.getAncestryKey());
    }

    /**
     * Test UTF characters and missing columns.
     */
    @Test
    public void testUTFAndMissingColumns() {
        GeoName howzEHajiGeo = howzEHaji.geoName;
        assertEquals("incorrect geonameID", 1139905, howzEHajiGeo.getGeonameID());
        assertEquals("incorrect name", "Ḩowẕ-e Ḩājī Bēg", howzEHajiGeo.getName());
        assertEquals("incorrect asciiName", "Howz-e Haji Beg", howzEHajiGeo.getAsciiName());
        assertEquals("incorrect alternateNames", Arrays.asList("Hawdze Hajibeg","Howz-e Haji Beg","Howz-e Hajjibeyg","H̱awdze Ḩājibeg","حوض حاجی بېگ","Ḩowẕ-e Ḩājjībeyg","Ḩowẕ-e Ḩājī Bēg"), howzEHajiGeo.getAlternateNames());
        assertEquals("incorrect latitude", 34.90489, howzEHajiGeo.getLatitude(), 0.1);
        assertEquals("incorrect longitude", 64.10312, howzEHajiGeo.getLongitude(), 0.1);
    }

    /**
     * Test seldom used fields.
     */
    @Test
    public void testSeldomUsedFields() {
        GeoName strabenhausGeo = strabenhaus.geoName;
        assertEquals("incorrect geonameID", 2826158, strabenhausGeo.getGeonameID());
        assertEquals("incorrect alternateNames", new ArrayList<String>(), strabenhausGeo.getAlternateNames());
        assertEquals("incorrect adminCode3", "07138", strabenhausGeo.getAdmin3Code());
        assertEquals("incorrect adminCode4", "07138071", strabenhausGeo.getAdmin4Code());
        assertEquals("incorrect ancestry key", "DE.08.00.07138.07138071", strabenhausGeo.getAncestryKey());
        assertEquals("incorrect parent ancestry key", "DE.08.00.07138", strabenhausGeo.getParentAncestryKey());
    }

    /**
     * Test no primary country code.
     */
    @Test
    public void testNoPrimaryCountryCode() {
        GeoName noMansLandGeo = noMansLand.geoName;
        assertEquals("incorrect primaryCountryCode", CountryCode.NULL, noMansLandGeo.getPrimaryCountryCode());
        assertNull("if no country code, ancestry path should be null", noMansLandGeo.getParentAncestryKey());
        assertTrue("ancestry should be resolved", noMansLandGeo.isAncestryResolved());
    }

    /**
     * Test non-empty alternate country codes.
     */
    @Test
    public void testNonEmptyAlternateCountryCodes() {
        assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.US, CountryCode.MX),
                chihuahuaDesert.geoName.getAlternateCountryCodes());
    }

    /**
     * Test malformed alternate country codes.
     */
    @Test
    public void testMalformedAlternateCountryCodes() {
        assertEquals("incorrect alternateCountryCodes", Arrays.asList(CountryCode.PS),
                rasSalim.geoName.getAlternateCountryCodes());
    }

    /**
     * Test no feature code.
     */
    @Test
    public void testNoFeatureCode() {
        assertEquals("incorrect featureCode", FeatureCode.NULL, murrayCanyon.geoName.getFeatureCode());
        assertNotNull("parent ancestry key should not be null", murrayCanyon.geoName.getParentAncestryKey());
    }

    /**
     * Test ancestry keys for top-level administrative division.
     */
    @Test
    public void testTopLevelAncestryKeys() {
        assertEquals("incorrect ancestry key", "US", unitedStates.geoName.getAncestryKey());
        assertNull("parent ancestry key should be null", unitedStates.geoName.getParentAncestryKey());
    }

    /**
     * Test isTopLevelAdminDivision.
     */
    @Test
    public void testIsTopLevelAdminDivision() {
        assertFalse("[Gun Barrel City] is not a top level admin division", gunBarrelCity.geoName.isTopLevelAdminDivision());
        assertTrue("[Antarctica] is a top level admin division", antarctica.geoName.isTopLevelAdminDivision());
        assertTrue("[American Samoa] is a top level admin division", americanSamoa.geoName.isTopLevelAdminDivision());
        assertFalse("[Coral Sea Islands] is not a top level admin division", coralSeaIslands.geoName.isTopLevelAdminDivision());
        assertTrue("[United States] is a top level admin division", unitedStates.geoName.isTopLevelAdminDivision());
        assertFalse("[Virginia] is not a top level admin division", virginia.geoName.isTopLevelAdminDivision());
        assertFalse("[Campo] is not a top level admin division", campoParish.geoName.isTopLevelAdminDivision());
    }

    /**
     * Test isToplevelTerritory.
     */
    @Test
    public void testIsTopLevelTerritory() {
        assertFalse("Non-territory [Gun Barrel City] should not be a top level territory", gunBarrelCity.geoName.isTopLevelTerritory());
        assertTrue("[Antarctica] should be a top level territory", antarctica.geoName.isTopLevelTerritory());
        assertTrue("[American Samoa] should be a top level territory", americanSamoa.geoName.isTopLevelTerritory());
        assertFalse("[Coral Sea Islands] should not be a top level territory", coralSeaIslands.geoName.isTopLevelTerritory());
    }

    /**
     * Test correct ancestry keys for territories.
     */
    @Test
    public void testTerritoryAncestryKeys() {
        assertEquals("incorrect ancestry key for top-level territory", "AQ", antarctica.geoName.getAncestryKey());
        assertNull("parent ancestry key of top-level territory should be null", antarctica.geoName.getParentAncestryKey());

        assertEquals("incorrect ancestry key for top-level territory", "AS", americanSamoa.geoName.getAncestryKey());
        assertNull("parent ancestry key of top-level territory should be null", americanSamoa.geoName.getParentAncestryKey());

        assertNull("ancestry key for non-top-level territory should be null", coralSeaIslands.geoName.getAncestryKey());
        assertEquals("incorrect parent ancestry key for non-top-level territory", "AU", coralSeaIslands.geoName.getParentAncestryKey());
    }

    /**
     * Test correct ancestry keys for parishes.
     */
    @Test
    public void testParishAncestryKeys() {
        assertNull("ancestry key for parish should be null", campoParish.geoName.getAncestryKey());
        assertEquals("incorrect parent ancestry key for parish", "ES", campoParish.geoName.getParentAncestryKey());
    }

    /**
     * Test ancestry resolution check when no parents are resolved.
     */
    @Test
    public void testIsAncestryResolved_NoParents() {
        // verify correct resolution response when no parents are set
        assertFalse("no parent set [reston], should not be resolved", reston.geoName.isAncestryResolved());
        assertFalse("no parent set [fairfax county], should not be resolved", fairfaxCounty.geoName.isAncestryResolved());
        assertFalse("no parent set [virginia], should not be resolved", virginia.geoName.isAncestryResolved());
        assertTrue("no parent set [united states] but top level, should be resolved", unitedStates.geoName.isAncestryResolved());
        assertTrue("no parent set [american samoa] but top level, should be resolved", americanSamoa.geoName.isAncestryResolved());
        assertFalse("no parent set [coral sea islands], should not be resolved", coralSeaIslands.geoName.isAncestryResolved());
    }

    /**
     * Test ancestry resolution when only part of the ancestry is resolved.
     */
    @Test
    public void testIsAncestryResolved_PartialResolution() {
        GeoName restonGeo = reston.geoName;
        BasicGeoName fairfaxCountyGeo = fairfaxCounty.geoName;
        BasicGeoName virginiaGeo = virginia.geoName;
        restonGeo.setParent(fairfaxCountyGeo);
        assertFalse("only one parent set [reston], should not be resolved", restonGeo.isAncestryResolved());
        fairfaxCountyGeo.setParent(virginiaGeo);
        assertFalse("only two parents set [reston], should not be resolved", restonGeo.isAncestryResolved());
        assertFalse("only one parent set [fairfax county], should not be resolved", fairfaxCountyGeo.isAncestryResolved());
    }

    /**
     * Test ancestry resolution when all ancestry is resolved.
     */
    @Test
    public void testIsAncestryResolved_FullResolution() {
        GeoName restonGeo = reston.geoName;
        BasicGeoName fairfaxCountyGeo = fairfaxCounty.geoName;
        BasicGeoName virginiaGeo = virginia.geoName;
        BasicGeoName unitedStatesGeo = unitedStates.geoName;
        GeoName coralSeaIslandsGeo = coralSeaIslands.geoName;

        virginiaGeo.setParent(unitedStatesGeo);
        fairfaxCountyGeo.setParent(virginiaGeo);
        restonGeo.setParent(fairfaxCountyGeo);
        assertTrue("[reston] should be fully resolved", restonGeo.isAncestryResolved());
        assertTrue("[fairfax county] should be fully resolved", fairfaxCountyGeo.isAncestryResolved());
        assertTrue("[virginia] should be fully resolved", virginiaGeo.isAncestryResolved());
        assertTrue("[united states] should be fully resolved", unitedStatesGeo.isAncestryResolved());
        assertEquals("incorrect gazetteer records with ancestry", String.format("%s\n%s\n%s\n%s", reston.gazetteerRecord,
                fairfaxCounty.gazetteerRecord, virginia.gazetteerRecord, unitedStates.gazetteerRecord),
                restonGeo.getGazetteerRecordWithAncestry());

        coralSeaIslandsGeo.setParent(australia.geoName);
        assertTrue("[coral sea islands] should be fully resolved", coralSeaIslandsGeo.isAncestryResolved());
    }

    /**
     * Test invalid parent configuration.
     */
    @Test
    public void testSetParent_InvalidParent() {
        BasicGeoName restonGeo = reston.geoName;

        assertNull("[reston] should have no parent", restonGeo.getParent());
        assertFalse("non-administrative parent should not be allowed", restonGeo.setParent(boston.geoName));
        assertNull("[reston] should have no parent", restonGeo.getParent());
        assertTrue("non-direct parent should be allowed", restonGeo.setParent(virginia.geoName));
        assertEquals("[reston] should have parent [virginia]", virginia.geoName, restonGeo.getParent());
        assertFalse("unrelated parent should not be allowed", restonGeo.setParent(australia.geoName));
        assertEquals("[reston] should have parent [virginia]", virginia.geoName, restonGeo.getParent());
        assertTrue("direct parent should be allowed", restonGeo.setParent(fairfaxCounty.geoName));
        assertEquals("[reston] should have parent [fairfax county]", fairfaxCounty.geoName, restonGeo.getParent());
        assertFalse("null parent should result in a no-op", restonGeo.setParent(null));
        assertEquals("[reston] should have parent [fairfax county]", fairfaxCounty.geoName, restonGeo.getParent());
        assertFalse("cannot set parent to self", restonGeo.setParent(restonGeo));
        assertEquals("[reston] should have parent [fairfax county]", fairfaxCounty.geoName, restonGeo.getParent());
    }

    /**
     * Parse a "bad" gazetteer record to make sure exceptions are
     * properly caught and handled.
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testParseExceptions() throws IOException, ParseException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File("./src/test/resources/gazetteers/BadGeoNamesSample.txt")), "UTF-8"));
        String line;
        List<GeoName> geonames = new ArrayList<GeoName>();
        while ((line = r.readLine()) != null)
            geonames.add(BasicGeoName.parseFromGeoNamesRecord(line));
        r.close();

        // if no exceptions are thrown, the test is assumed to have succeeded
    }

}
