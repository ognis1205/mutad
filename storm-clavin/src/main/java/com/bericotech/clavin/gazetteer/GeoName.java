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
 * GeoName.java
 *
 *###################################################################*/

package com.bericotech.clavin.gazetteer;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Data-rich representation of a named location, based on entries in
 * the GeoNames gazetteer.
 *
 * TODO: link administrative subdivision code fields to the GeoName
 *       records they reference
 *
 */
public interface GeoName {
    /**
     * sentinel value used in place of null when numeric value in
     * GeoNames record is not provided (see: geonameID, latitude,
     * longitude, population, elevation, digitalElevationModel)
     */
    int OUT_OF_BOUNDS = -9999999;

    /**
     * Get the name of the country associated with the primary country code.
     * @return the name of the primary country
     */
    String getPrimaryCountryName();

    /**
     * Get the ancestry key that can be used to identify the direct administrative
     * parent of this GeoName.  See {@link com.bericotech.clavin.gazetteer.GeoName#getAncestryKey()} for a description
     * of an ancestry key.
     *
     * For example, the GeoName "Reston, VA" is found in
     * the hierarchy:
     *
     * <ul>
     *   <li>Reston</li>
     *   <li><ul>
     *     <li>Fairfax County (admin2: "059")</li>
     *     <li><ul>
     *       <li>Virginia (admin1: "VA")</li>
     *       <li><ul>
     *         <li>United States (country code: "US")</li>
     *       </ul></li>
     *     </ul></li>
     *   </ul></li>
     * </ul>
     *
     * Its parent ancestor key is "US.VA.059", which is the key returned by
     * {@link com.bericotech.clavin.gazetteer.GeoName#getAncestryKey()} for the GeoName "Fairfax County."
     *
     * @return the ancestry key of the direct administrative parent of this GeoName; will be
     *         <code>null</code> for top-level elements such as countries
     */
    String getParentAncestryKey();

    /**
     * Get the ancestry key that can be used to identify this administrative division.
     * This method returns <code>null</code> for all feature types except the following
     * {@link com.bericotech.clavin.gazetteer.FeatureClass#A} records:
     * <ul>
     *   <li>Country ({@link com.bericotech.clavin.gazetteer.FeatureCode#PCL})</li>
     *   <li>First Administrative Division ({@link com.bericotech.clavin.gazetteer.FeatureCode#ADM1})</li>
     *   <li>Second Administrative Division ({@link com.bericotech.clavin.gazetteer.FeatureCode#ADM2})</li>
     *   <li>Third Administrative Division ({@link com.bericotech.clavin.gazetteer.FeatureCode#ADM3})</li>
     *   <li>Fourth Administrative Division ({@link com.bericotech.clavin.gazetteer.FeatureCode#ADM4})</li>
     * </ul>
     *
     * The ancestry key includes the country and administrative division codes for all
     * GeoNames in the ancestry path up to and including this GeoName.  For example,
     * the GeoName "Fairfax County" is found in the hierarchy:
     *
     * <ul>
     *   <li>Fairfax County (admin2: "059")</li>
     *   <li><ul>
     *     <li>Virginia (admin1: "VA")</li>
     *     <li><ul>
     *       <li>United States (country code: "US")</li>
     *     </ul></li>
     *   </ul></li>
     * </ul>
     *
     * Its ancestry key is "US.VA.059".
     *
     * If no code is configured for the level of this administrative division, it will not be
     * considered to have an ancestry key and will not be able to be referenced as the parent
     * of other locations.  (e.g. If this is an ADM4 and there is no admin4Code, getAncestryKey()
     * will return <code>null</code>)
     *
     * @return the ancestry key for this administrative division or <code>null</code> if this
     *         GeoName is not an administrative division or its ancestry key cannot be derived.
     */
    String getAncestryKey();

    /**
     * Is this GeoName a top-level administrative division (e.g. country, territory or similar)?
     * @return <code>true</code> if this is a top-level administrative division
     */
    boolean isTopLevelAdminDivision();

    /**
     * Is this GeoName a top-level territory? A GeoName is considered to be a
     * top-level territory if it is an A:TERR record and the name assigned to
     * its primary country code is either the name of the GeoName or one of its
     * configured alternate names.
     * @return <code>true</code> if the GeoName is a top level territory
     */
    boolean isTopLevelTerritory();

    /**
     * Is this GeoName a descendant of the provided GeoName?
     * @param geoname the GeoName to test
     * @return <code>true</code> if the provided GeoName is hierarchically an ancestor of this GeoName
     */
    boolean isDescendantOf(GeoName geoname);

    /**
     * Is this GeoName an ancestor of the provided GeoName?
     * @param geoname the GeoName to test
     * @return <code>true</code> if this GeoName is hierarchically an ancestor of the provided GeoName
     */
    boolean isAncestorOf(GeoName geoname);

    /**
     * Get the ID of the parent of this GeoName.
     * @return the ID of the parent of this GeoName
     */
    Integer getParentId();

    /**
     * Get the parent of this GeoName.
     * @return the configured parent of this GeoName
     */
    GeoName getParent();

    /**
     * Set the parent of this GeoName.
     * @param prnt the parent; if provided, parent.getAncestryKey() must
     *             match this.getParentAncestryKey(); <code>null</code>
     *             is ignored
     * @return <code>true</code> if the parent was set, <code>false</code> if
     *         the parent was not the valid parent for this GeoName
     */
    boolean setParent(GeoName prnt);

    /**
     * Check to see if the ancestry hierarchy has been completely resolved for this GeoName.
     * @return <code>true</code> if all administrative parents have been resolved
     */
    boolean isAncestryResolved();

    /**
     * Get the ID of the record in geonames database.
     * @return the ID
     */
    int getGeonameID();

    /**
     * Get the name of the geographical point (UTF-8).
     * @return the name
     */
    String getName();

    /**
     * Get the name of the geographical point in plain ascii characters.
     * @return the plain ascii name
     */
    String getAsciiName();

    /**
     * Get the alternate names for the location.
     * @return the alternate names; an empty List if none
     */
    List<String> getAlternateNames();

    /**
     * Gets the preferred name of this GeoName, if configured,
     * otherwise returns the name.
     * @return the preferred name
     */
    String getPreferredName();

    /**
     * Get the latitude in decimal degrees.
     * @return the latitude
     */
    double getLatitude();

    /**
     * Get the longitude in decimal degrees.
     * @return the longitude
     */
    double getLongitude();

    /**
     * Get the major feature category.
     * See http://www.geonames.org/export/codes.html
     * @return the major feature category
     */
    FeatureClass getFeatureClass();

    /**
     * Get the feature code.
     * See http://www.geonames.org/export/codes.html
     * @return the feature code
     */
    FeatureCode getFeatureCode();

    /**
     * Get the primary ISO-3166 2-letter country code.
     * @return the primary country code
     */
    CountryCode getPrimaryCountryCode();

    /**
     * Get the alternate ISO-3166 2-letter country codes.
     * @return the alternate country codes; empty List if none
     */
    List<CountryCode> getAlternateCountryCodes();

    /**
     * Get the code for the first level administrative division (e.g. a
     * state in the US). This is mostly FIPS codes. ISO codes are
     * used for US, CH, BE and ME. UK and Greece are using an
     * additional level between country and FIPS code.
     * @return the code for the first administrative division
     */
    String getAdmin1Code();

    /**
     * Get the code for the second level administrative division (e.g. a
     * county in the US).
     * @return the code for the second administrative division
     */
    String getAdmin2Code();

    /**
     * Get the code for the third level administrative division.
     * @return the code for the third administrative division
     */
    String getAdmin3Code();

    /**
     * Get the code for the fourth level administrative division.
     * @return the code for the fourth administrative division
     */
    String getAdmin4Code();

    /**
     * Get the total number of inhabitants.
     * @return the population
     */
    long getPopulation();

    /**
     * Get the elevation in meters.
     * @return the elevation
     */
    int getElevation();

    /**
     * Get the digital elevation model, srtm3 or gtopo30, average
     * elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m)
     * area in meters, integer.  srtm processed by cgiar/ciat.
     * @return the average elevation in meters
     */
    int getDigitalElevationModel();

    /**
     * Get the time zone for the geographical point.
     * @return the time zone; may be <code>null</code>
     */
    TimeZone getTimezone();

    /**
     * Get the last modification date in the GeoNames database.
     * @return the last modification date; may be <code>null</code>
     */
    Date getModificationDate();

    /**
     * Get the gazetteer record for this GeoName.
     * @return the gazetteer record this GeoName was parsed from
     */
    String getGazetteerRecord();

    /**
     * Get the gazetteer records for this GeoName and its ancestors, separated
     * by newline characters.
     * @return the newline-separated gazetteer records for this GeoName and its ancestors.
     */
    String getGazetteerRecordWithAncestry();
}
