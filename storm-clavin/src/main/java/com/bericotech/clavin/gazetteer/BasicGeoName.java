package com.bericotech.clavin.gazetteer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * BasicGeoName.java
 *
 *###################################################################*/

/**
 * Data-rich representation of a named location, based on entries in
 * the GeoNames gazetteer.
 *
 * TODO: link administrative subdivision code fields to the GeoName
 *       records they reference
 *
 */
public class BasicGeoName implements GeoName {
    /**
     * The logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BasicGeoName.class);

    /**
     * The regex used to extract the administrative division level for A:ADM[1-4]H? records
     */
    private static final Pattern ADM_LEVEL_REGEX = Pattern.compile("^ADM(\\d)H?$");

    /**
     * The set of top-level feature codes.
     */
    private static final Set<FeatureCode> TOP_LEVEL_FEATURES = EnumSet.of(
            FeatureCode.PCL,
            FeatureCode.PCLD,
            FeatureCode.PCLF,
            FeatureCode.PCLI,
            FeatureCode.PCLIX,
            FeatureCode.PCLS,
            FeatureCode.TERRI
    );

    /**
     * The set of FeatureCodes that are valid administrative ancestors.
     */
    private static final Set<FeatureCode> VALID_ADMIN_ANCESTORS = EnumSet.of(
            FeatureCode.ADM1,
            FeatureCode.ADM2,
            FeatureCode.ADM3,
            FeatureCode.ADM4,
            FeatureCode.PCL,
            FeatureCode.PCLD,
            FeatureCode.PCLF,
            FeatureCode.PCLI,
            FeatureCode.PCLIX,
            FeatureCode.PCLS,
            FeatureCode.TERRI
    );

    // id of record in geonames database
    private final int geonameID;

    // name of geographical point (utf8)
    private final String name;

    // name of geographical point in plain ascii characters
    private final String asciiName;

    // list of alternate names for location
    private final List<String> alternateNames;

    // the preferred name of this GeoName
    private final String preferredName;

    // latitude in decimal degrees
    private final double latitude;

    // longitude in decimal degrees
    private final double longitude;

    // major feature category
    // (see http://www.geonames.org/export/codes.html)
    private final FeatureClass featureClass;

    // http://www.geonames.org/export/codes.html
    private final FeatureCode featureCode;

    // ISO-3166 2-letter country code
    private final CountryCode primaryCountryCode;

    // associated name with country code
    @Override
    public String getPrimaryCountryName(){
        return  primaryCountryCode.name;
    }

    // list of alternate ISO-3166 2-letter country codes
    private final List<CountryCode> alternateCountryCodes;

    /*  TODO: refactor the 4 fields below to link to the GeoName
     *        object that they refer to
     */

    // Mostly FIPS codes. ISO codes are used for US, CH, BE and ME. UK
    // and Greece are using an additional level between country and
    // FIPS code.
    private final String admin1Code;

    // code for the second administrative division
    // (e.g., a county in the US)
    private final String admin2Code;

    // code for third level administrative division
    private final String admin3Code;

    // code for fourth level administrative division
    private final String admin4Code;

    // total number of inhabitants
    private final long population;

    // in meters
    private final int elevation;

    // digital elevation model, srtm3 or gtopo30, average elevation of
    // 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters,
    // integer. srtm processed by cgiar/ciat.
    private final int digitalElevationModel;

    // timezone for geographical point
    private final TimeZone timezone;

    // date of last modification in GeoNames database
    private final Date modificationDate;

    // the GeoName ID of the parent of this GeoName
    private Integer parentId;

    // the parent of this GeoName
    private GeoName parent;

    // the gazetteer record this GeoName was parsed from
    private String gazetteerRecord;

    /**
     * Sole constructor for {@link BasicGeoName} class.
     *
     * Encapsulates a gazetteer record from the GeoNames database.
     *
     * @param geonameID                 unique identifier
     * @param name                      name of this location
     * @param asciiName                 plain text version of name
     * @param alternateNames            list of alternate names, if any
     * @param preferredName             the preferred name, if known
     * @param latitude                  lat coord
     * @param longitude                 lon coord
     * @param featureClass              general type of feature (e.g., "Populated place")
     * @param featureCode               specific type of feature (e.g., "capital of a political entity")
     * @param primaryCountryCode        ISO country code
     * @param alternateCountryCodes     list of alternate country codes, if any (i.e., disputed territories)
     * @param admin1Code                FIPS code for first-level administrative subdivision (e.g., state or province)
     * @param admin2Code                second-level administrative subdivision (e.g., county)
     * @param admin3Code                third-level administrative subdivision
     * @param admin4Code                fourth-level administrative subdivision
     * @param population                number of inhabitants
     * @param elevation                 elevation in meters
     * @param digitalElevationModel     another way to measure elevation
     * @param timezone                  timezone for this location
     * @param modificationDate          date of last modification for the GeoNames record
     * @param gazetteerRecord           the gazetteer record
     */
    public BasicGeoName(
            int geonameID,
            String name,
            String asciiName,
            List<String> alternateNames,
            String preferredName,
            Double latitude,
            Double longitude,
            FeatureClass featureClass,
            FeatureCode featureCode,
            CountryCode primaryCountryCode,
            List<CountryCode> alternateCountryCodes,
            String admin1Code,
            String admin2Code,
            String admin3Code,
            String admin4Code,
            Long population,
            Integer elevation,
            Integer digitalElevationModel,
            TimeZone timezone,
            Date modificationDate,
            String gazetteerRecord) {
        this.geonameID = geonameID;
        this.name = name;
        this.asciiName = asciiName;
        if (alternateNames != null) {
            // defensive copy
            this.alternateNames = Collections.unmodifiableList(new ArrayList<String>(alternateNames));
        } else {
            // ensure this is never null
            this.alternateNames = Collections.EMPTY_LIST;
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.primaryCountryCode = primaryCountryCode;
        String pccName = primaryCountryCode != null ? primaryCountryCode.name : "";
        if (alternateCountryCodes != null) {
            // defensive copy
            this.alternateCountryCodes = Collections.unmodifiableList(new ArrayList<CountryCode>(alternateCountryCodes));
        } else {
            // ensure this is never null
            this.alternateCountryCodes = Collections.EMPTY_LIST;
        }
        this.featureClass = featureClass;
        // configure the feature code so top-level territories are distinguishable
        if (featureCode == FeatureCode.TERR) {
            boolean topLevel = (this.name != null && !this.name.isEmpty() && this.name.equals(pccName)) ||
                    (this.asciiName != null && !this.asciiName.isEmpty() && this.asciiName.equals(pccName)) ||
                    this.alternateNames.contains(pccName);
            this.featureCode = topLevel ? FeatureCode.TERRI : FeatureCode.TERR;
        } else {
            this.featureCode = featureCode;
        }
        // if this is a top level division, use the primary country name as the preferred name; otherwise
        // use the name provided or null
        boolean usePcc = TOP_LEVEL_FEATURES.contains(featureCode) && !pccName.isEmpty() &&
                ((this.name != null && !this.name.isEmpty() && this.name.equals(pccName)) ||
                (this.asciiName != null && !this.asciiName.isEmpty() && this.asciiName.equals(pccName)) ||
                this.alternateNames.contains(pccName));
        if (usePcc) {
            this.preferredName = pccName;
        } else {
            this.preferredName = preferredName != null && !preferredName.trim().isEmpty() ? preferredName.trim() : null;
        }

        this.admin1Code = admin1Code;
        this.admin2Code = admin2Code;
        this.admin3Code = admin3Code;
        this.admin4Code = admin4Code;
        this.population = population;
        this.elevation = elevation;
        this.digitalElevationModel = digitalElevationModel;
        this.timezone = timezone != null ? (TimeZone) timezone.clone() : null;
        this.modificationDate = modificationDate != null ? new Date(modificationDate.getTime()) : null;
        this.gazetteerRecord = gazetteerRecord;
    }


    /**
     * Builds a {@link BasicGeoName} object based on a single gazetteer
     * record in the GeoNames geographical database.
     *
     * @param inputLine     single line of tab-delimited text representing one record from the GeoNames gazetteer
     * @return              new GeoName object
     */
    public static GeoName parseFromGeoNamesRecord(String inputLine) {
        return parseFromGeoNamesRecord(inputLine, null);
    }

    /**
     * Builds a {@link BasicGeoName} object based on a single gazetteer
     * record in the GeoNames geographical database.
     *
     * @param inputLine     single line of tab-delimited text representing one record from the GeoNames gazetteer
     * @param preferredName the preferred name of this GeoName as indicated by the GeoNames alternate names table
     * @return              new GeoName object
     */
    public static GeoName parseFromGeoNamesRecord(final String inputLine, final String preferredName) {
        String[] ancestry = inputLine.split("\n");
        GeoName geoName = parseGeoName(ancestry[0], preferredName);
        // if more records exist, assume they are the ancestory of the target GeoName
        if (ancestry.length > 1) {
            GeoName current = geoName;
            for (int idx = 1; idx < ancestry.length; idx++) {
                GeoName parent = parseGeoName(ancestry[idx], null);
                if (!current.setParent(parent)) {
                    LOG.error("Invalid ancestry path for GeoName [{}]: {}", geoName, inputLine.replaceAll("\n", " |@| "));
                    break;
                }
                current = parent;
            }
        }
        return geoName;
    }

    private static GeoName parseGeoName(final String inputLine, final String preferredName) {
        // GeoNames gazetteer entries are tab-delimited
        String[] tokens = inputLine.split("\t");

        // initialize each field with the corresponding token
        int geonameID = Integer.parseInt(tokens[0]);
        String name = tokens[1];
        String asciiName = tokens[2];

        List<String> alternateNames;
        if (tokens[3].length() > 0) {
            // better to pass empty array than array containing empty String ""
            alternateNames = Arrays.asList(tokens[3].split(","));
        } else alternateNames = new ArrayList<String>();

        double latitude;
        try {
            latitude = Double.parseDouble(tokens[4]);
        } catch (NumberFormatException e) {
            latitude = OUT_OF_BOUNDS;
        }

        double longitude;
        try {
            longitude = Double.parseDouble(tokens[5]);
        } catch (NumberFormatException e) {
            longitude = OUT_OF_BOUNDS;
        }

        FeatureClass featureClass;
        if (tokens[6].length() > 0) {
            featureClass = FeatureClass.valueOf(tokens[6]);
        } else featureClass = FeatureClass.NULL; // not available

        FeatureCode featureCode;
        if (tokens[7].length() > 0) {
            featureCode = FeatureCode.valueOf(tokens[7]);
        } else featureCode = FeatureCode.NULL; // not available

        CountryCode primaryCountryCode;
        if (tokens[8].length() > 0) {
            primaryCountryCode = CountryCode.valueOf(tokens[8]);
        } else primaryCountryCode = CountryCode.NULL; // No Man's Land

        List<CountryCode> alternateCountryCodes = new ArrayList<CountryCode>();
        if (tokens[9].length() > 0) {
            // don't pass list only containing empty String ""
            for (String code : tokens[9].split(",")) {
                if (code.length() > 0) // check for malformed data
                    alternateCountryCodes.add(CountryCode.valueOf(code));
            }
        }

        String admin1Code = tokens[10];
        String admin2Code = tokens[11];

        String admin3Code;
        String admin4Code;
        long population;
        int elevation;
        int digitalElevationModel;
        TimeZone timezone;
        Date modificationDate;

        // check for dirty data...
        if (tokens.length < 19) {
            // GeoNames record format is corrupted, don't trust any
            // data after this point
            admin3Code = "";
            admin4Code = "";
            population = OUT_OF_BOUNDS;
            elevation = OUT_OF_BOUNDS;
            digitalElevationModel = OUT_OF_BOUNDS;
            timezone = null;
            modificationDate = new Date(0);
        } else { // everything looks ok, soldiering on...
            admin3Code = tokens[12];
            admin4Code = tokens[13];
            try {
                population = Long.parseLong(tokens[14]);
            } catch (NumberFormatException e) {
                population = OUT_OF_BOUNDS;
            }
            try {
                elevation = Integer.parseInt(tokens[15]);
            } catch (NumberFormatException e) {
                elevation = OUT_OF_BOUNDS;
            }
            try {
                digitalElevationModel = Integer.parseInt(tokens[16]);
            } catch (NumberFormatException e) {
                digitalElevationModel = OUT_OF_BOUNDS;
            }
            timezone = TimeZone.getTimeZone(tokens[17]);
            try {
                modificationDate = new SimpleDateFormat("yyyy-MM-dd").parse(tokens[18]);
            } catch (ParseException e) {
                modificationDate = new Date(0);
            }
        }

        return new BasicGeoName(geonameID, name, asciiName, alternateNames, preferredName,
                latitude, longitude, featureClass, featureCode,
                primaryCountryCode, alternateCountryCodes, admin1Code,
                admin2Code, admin3Code, admin4Code, population,
                elevation, digitalElevationModel, timezone,
                modificationDate, inputLine);
    }

    private static int getAdminLevel(final FeatureClass fClass, final FeatureCode fCode) {
        return getAdminLevel(fClass, fCode, null, null, null);
    }

    private static int getAdminLevel(final FeatureClass fClass, final FeatureCode fCode, final String geoname, final List<String> altNames, final String countryName) {
        int admLevel = Integer.MAX_VALUE;
        if (fClass == FeatureClass.A) {
            if (fCode == null) {
                admLevel = -1;
            } else if (fCode == FeatureCode.TERR) {
                admLevel = 1;
            } else if (fCode == FeatureCode.PRSH) {
                admLevel = 1;
            } else if (TOP_LEVEL_FEATURES.contains(fCode)) {
                admLevel = 0;
            } else {
                Matcher matcher = ADM_LEVEL_REGEX.matcher(fCode.name());
                if (matcher.matches()) {
                    admLevel = Integer.parseInt(matcher.group(1));
                }
            }
        }
        return admLevel;
    }

    /**
     * For pretty-printing.
     *
     */
    @Override
    public String toString() {
        return getPreferredName() + " (" + getPrimaryCountryName() + ", " + admin1Code + ")" + " [pop: " + population + "] <" + geonameID + ">";
    }

    @Override
    public String getParentAncestryKey() {
        String key = buildAncestryKey(FeatureCode.ADM4, false);
        // return null if the key is empty; that means we are a top-level administrative component
        return !key.isEmpty() ? key : null;
    }

    @Override
    public String getAncestryKey() {
        boolean hasKey = featureClass == FeatureClass.A && VALID_ADMIN_ANCESTORS.contains(featureCode);
        if (hasKey) {
            String myCode;
            switch (featureCode) {
                case ADM1:
                    myCode = admin1Code;
                    break;
                case ADM2:
                    myCode = admin2Code;
                    break;
                case ADM3:
                    myCode = admin3Code;
                    break;
                case ADM4:
                    myCode = admin4Code;
                    break;
                case PCL:
                case PCLD:
                case PCLF:
                case PCLI:
                case PCLIX:
                case PCLS:
                case TERRI:
                    myCode = primaryCountryCode != null ? primaryCountryCode.name() : null;
                    break;
                default:
                    myCode = null;
                    break;
            }
            hasKey = myCode != null && !myCode.trim().isEmpty();
        }
        String key = (hasKey ? buildAncestryKey(FeatureCode.ADM4, true) : "").trim();
        return !key.isEmpty() ? key : null;
    }

    @Override
    public boolean isTopLevelAdminDivision() {
        return TOP_LEVEL_FEATURES.contains(featureCode);
    }

    @Override
    public boolean isTopLevelTerritory() {
        return featureCode == FeatureCode.TERRI;
    }

    /**
     * Recursively builds the ancestry key for this GeoName, optionally including the
     * key for this GeoName's administrative division if requested and applicable. See
     * {@link BasicGeoName#getAncestryKey()} for a description of the ancestry key. Only
     * divisions that have a non-empty code set in this GeoName will be included in the
     * key.
     * @param level the administrative division at the end of the key (e.g. ADM2 to build
     *              the key COUNTRY.ADM1.ADM2)
     * @param includeSelf <code>true</code> to include this GeoName's code in the key
     * @return the generated ancestry key
     */
    private String buildAncestryKey(final FeatureCode level, final boolean includeSelf) {
        // if we have reached the root level, stop
        if (level == null) {
            return "";
        }

        String keyPart;
        FeatureCode nextLevel;
        switch (level) {
            case ADM4:
                keyPart = admin4Code;
                nextLevel = FeatureCode.ADM3;
                break;
            case ADM3:
                keyPart = admin3Code;
                nextLevel = FeatureCode.ADM2;
                break;
            case ADM2:
                keyPart = admin2Code;
                nextLevel = FeatureCode.ADM1;
                break;
            case ADM1:
                // territories will be considered level 1 if they have the same country code as their
                // parent but cannot contain descendants so there should be no keypart for this level;
                // all parishes are considered to be direct descendants of their containing country with
                // no descendants; they should not have a key part at this level
                keyPart = featureCode != FeatureCode.TERR && featureCode != FeatureCode.PRSH ? admin1Code : "";
                nextLevel = FeatureCode.PCL;
                break;
            case PCL:
                keyPart = primaryCountryCode != null && primaryCountryCode != CountryCode.NULL ? primaryCountryCode.name() : "";
                nextLevel = null;
                break;
            default:
                throw new IllegalArgumentException("Level must be one of [PCL, ADM1, ADM2, ADM3, ADM4]");
        }
        keyPart = keyPart.trim();
        if (nextLevel != null && !keyPart.isEmpty()) {
            keyPart = String.format(".%s", keyPart);
        }
        int keyLevel = getAdminLevel(FeatureClass.A, level);
        int nameLevel = getAdminLevel(featureClass, featureCode, name, alternateNames, primaryCountryCode.name);

        // if the requested key part is a larger administrative division than the level of the
        // geoname or, if we are including the geoname's key part and it is the requested part,
        // include it in the ancestry key (if not blank); otherwise, move to the next level
        String qualifiedKey = (nameLevel > keyLevel || (includeSelf && keyLevel == nameLevel)) && !keyPart.isEmpty() ?
                String.format("%s%s", buildAncestryKey(nextLevel, includeSelf), keyPart) :
                buildAncestryKey(nextLevel, includeSelf);
        // if any part of the key is missing once a lower-level component has been specified, we cannot
        // resolve the ancestry path and an empty string should be returned.
        if (qualifiedKey.startsWith(".") || qualifiedKey.contains("..") || qualifiedKey.endsWith(".")) {
            qualifiedKey = "";
        }
        return qualifiedKey;
    }

    @Override
    public boolean isDescendantOf(final GeoName geoname) {
        boolean descended = false;
        if (geoname != null) {
            GeoName test;
            // empty for loop exits when parent is found or top level is reached
            for (test = this; test != null && !test.equals(geoname); test = test.getParent());
            descended = test != null;
        }
        return descended;
    }

    @Override
    public boolean isAncestorOf(final GeoName geoname) {
        return geoname != null && geoname.isDescendantOf(this);
    }

    @Override
    public GeoName getParent() {
        return parent;
    }

    @Override
    public boolean setParent(final GeoName prnt) {
        String myParentKey = this.getParentAncestryKey();
        String parentKey = prnt != null ? prnt.getAncestryKey() : null;
        boolean parentSet = false;
        if (prnt != null) {
            if (prnt.getFeatureClass() != FeatureClass.A || !VALID_ADMIN_ANCESTORS.contains(prnt.getFeatureCode())) {
                LOG.error(String.format("Invalid administrative parent type [%s:%s] specified for GeoName [%s]; Parent [%s]",
                        prnt.getFeatureClass(), prnt.getFeatureCode(), this, prnt));
            } else if (myParentKey != null && parentKey != null && !myParentKey.startsWith(parentKey)) {
                LOG.error(String.format("Parent ancestry key [%s] does not match the expected key [%s] for GeoName [%s]; Parent [%s]",
                        parentKey, myParentKey, this, prnt));
            } else if (this.equals(prnt)) {
                LOG.warn("Attempted to set parent to self: {}", prnt);
            } else {
                this.parent = prnt;
                parentSet = true;
            }
        }
        return parentSet;
    }

    @Override
    public Integer getParentId() {
        return parent != null ? parent.getGeonameID() : null;
    }

    @Override
    public boolean isAncestryResolved() {
        // this GeoName is considered resolved if it is a top level administrative division,
        // it is unresolvable, or all parents up to a top-level element have been configured
        return getAdminLevel(featureClass, featureCode, name, alternateNames, primaryCountryCode.name) <= 0 ||
                getParentAncestryKey() == null || (parent != null && parent.isAncestryResolved());
    }

    @Override
    public int getGeonameID() {
        return geonameID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAsciiName() {
        return asciiName;
    }

    @Override
    public List<String> getAlternateNames() {
        return alternateNames;
    }

    @Override
    public String getPreferredName() {
        return preferredName != null ? preferredName : name;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public FeatureClass getFeatureClass() {
        return featureClass;
    }

    @Override
    public FeatureCode getFeatureCode() {
        return featureCode;
    }

    @Override
    public CountryCode getPrimaryCountryCode() {
        return primaryCountryCode;
    }

    @Override
    public List<CountryCode> getAlternateCountryCodes() {
        return alternateCountryCodes;
    }

    @Override
    public String getAdmin1Code() {
        return admin1Code;
    }

    @Override
    public String getAdmin2Code() {
        return admin2Code;
    }

    @Override
    public String getAdmin3Code() {
        return admin3Code;
    }

    @Override
    public String getAdmin4Code() {
        return admin4Code;
    }

    @Override
    public long getPopulation() {
        return population;
    }

    @Override
    public int getElevation() {
        return elevation;
    }

    @Override
    public int getDigitalElevationModel() {
        return digitalElevationModel;
    }

    @Override
    public TimeZone getTimezone() {
        // defensive copy
        return timezone != null ? (TimeZone) timezone.clone() : null;
    }

    @Override
    public Date getModificationDate() {
        // defensive copy
        return modificationDate != null ? new Date(modificationDate.getTime()) : null;
    }

    @Override
    public String getGazetteerRecord() {
        return gazetteerRecord;
    }

    @Override
    public String getGazetteerRecordWithAncestry() {
        return parent != null ? String.format("%s\n%s", gazetteerRecord, parent.getGazetteerRecordWithAncestry()) : gazetteerRecord;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.geonameID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicGeoName other = (BasicGeoName) obj;
        if (this.geonameID != other.geonameID) {
            return false;
        }
        return true;
    }
}
