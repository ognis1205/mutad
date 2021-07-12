package com.bericotech.clavin.gazetteer;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.gazetteer.query.AncestryMode;
import com.bericotech.clavin.gazetteer.query.Gazetteer;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * This GeoName can be configured to lazily load its ancestry when its parent
 * is first requested or to load its ancestry only when explicitly requested
 * through a Gazetteer.
 */
public class LazyAncestryGeoName implements GeoName {
    /** The wrapped GeoName. */
    private final GeoName geoName;

    /** The GeoName ID of this GeoName's parent. */
    private final Integer parentId;

    /** The Gazetteer used to resolve the ancestry of the target GeoName. */
    private final Gazetteer gazetteer;

    /**
     * Creates a LazyAncestryGeoName whose ancestry must be manually loaded.
     * @param geoName the GeoName to wrap
     * @param parentId the ID of the parent of this GeoName
     */
    public LazyAncestryGeoName(final GeoName geoName, final Integer parentId) {
        this(geoName, parentId, null);
    }

    /**
     * Creates a LazyAncestryGeoName that lazily loads the ancestry of the wrapped
     * GeoName when its parent is first requested. If no Gazetteer is provided,
     * ancestry must be manually loaded.
     * @param geoName the GeoName to wrap
     * @param parentId the ID of the parent of this GeoName
     * @param gazetteer the Gazetteer used for ancestry resolution; if null, ancestry must be loaded manually
     */
    public LazyAncestryGeoName(final GeoName geoName, final Integer parentId, final Gazetteer gazetteer) {
        this.geoName = geoName;
        this.parentId = parentId;
        this.gazetteer = gazetteer;
    }

    @Override
    public Integer getParentId() {
        return parentId;
    }

    @Override
    public GeoName getParent() {
        if (gazetteer != null && parentId != null && !geoName.isAncestryResolved()) {
            try {
                geoName.setParent(gazetteer.getGeoName(parentId, AncestryMode.ON_CREATE));
            } catch (ClavinException ce) {
                throw new RuntimeException(String.format("Error lazy-loading ancestry for %s", geoName), ce);
            }
        }
        return geoName.getParent();
    }

    @Override
    public boolean setParent(GeoName prnt) {
        return geoName.setParent(prnt);
    }

    @Override
    public String getPrimaryCountryName() {
        return geoName.getPrimaryCountryName();
    }

    @Override
    public String getParentAncestryKey() {
        return geoName.getParentAncestryKey();
    }

    @Override
    public String getAncestryKey() {
        return geoName.getAncestryKey();
    }

    @Override
    public boolean isTopLevelAdminDivision() {
        return geoName.isTopLevelAdminDivision();
    }

    @Override
    public boolean isTopLevelTerritory() {
        return geoName.isTopLevelTerritory();
    }

    @Override
    public boolean isDescendantOf(GeoName geoname) {
        return geoName.isDescendantOf(geoname);
    }

    @Override
    public boolean isAncestorOf(GeoName geoname) {
        return geoName.isAncestorOf(geoname);
    }

    @Override
    public boolean isAncestryResolved() {
        return geoName.isAncestryResolved();
    }

    @Override
    public int getGeonameID() {
        return geoName.getGeonameID();
    }

    @Override
    public String getName() {
        return geoName.getName();
    }

    @Override
    public String getAsciiName() {
        return geoName.getAsciiName();
    }

    @Override
    public List<String> getAlternateNames() {
        return geoName.getAlternateNames();
    }

    @Override
    public String getPreferredName() {
        return geoName.getPreferredName();
    }

    @Override
    public double getLatitude() {
        return geoName.getLatitude();
    }

    @Override
    public double getLongitude() {
        return geoName.getLongitude();
    }

    @Override
    public FeatureClass getFeatureClass() {
        return geoName.getFeatureClass();
    }

    @Override
    public FeatureCode getFeatureCode() {
        return geoName.getFeatureCode();
    }

    @Override
    public CountryCode getPrimaryCountryCode() {
        return geoName.getPrimaryCountryCode();
    }

    @Override
    public List<CountryCode> getAlternateCountryCodes() {
        return geoName.getAlternateCountryCodes();
    }

    @Override
    public String getAdmin1Code() {
        return geoName.getAdmin1Code();
    }

    @Override
    public String getAdmin2Code() {
        return geoName.getAdmin2Code();
    }

    @Override
    public String getAdmin3Code() {
        return geoName.getAdmin3Code();
    }

    @Override
    public String getAdmin4Code() {
        return geoName.getAdmin4Code();
    }

    @Override
    public long getPopulation() {
        return geoName.getPopulation();
    }

    @Override
    public int getElevation() {
        return geoName.getElevation();
    }

    @Override
    public int getDigitalElevationModel() {
        return geoName.getDigitalElevationModel();
    }

    @Override
    public TimeZone getTimezone() {
        return geoName.getTimezone();
    }

    @Override
    public Date getModificationDate() {
        return geoName.getModificationDate();
    }

    @Override
    public String getGazetteerRecord() {
        return geoName.getGazetteerRecord();
    }

    @Override
    public String getGazetteerRecordWithAncestry() {
        return geoName.getGazetteerRecordWithAncestry();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LazyAncestryGeoName that = (LazyAncestryGeoName) o;

        if (!geoName.equals(that.geoName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return geoName.hashCode();
    }
}
