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
 * SearchLevel.java
 *
 *###################################################################*/

package com.bericotech.clavin.resolver.multipart;

import com.bericotech.clavin.gazetteer.FeatureClass;
import com.bericotech.clavin.gazetteer.FeatureCode;
import com.bericotech.clavin.gazetteer.GeoName;
import com.bericotech.clavin.gazetteer.query.QueryBuilder;

/**
 * A roll-up of feature classes and codes that assists in
 * searching for locations that fall into a particular category.
 */
public enum SearchLevel {
    COUNTRY,
    ADMIN1,
    ADMIN2,
    ADMIN3,
    ADMIN4,
    ADMINX,
    CITY;

    public static SearchLevel forGeoName(final GeoName name) {
        SearchLevel level = null;
        if (name != null) {
            if (name.isTopLevelAdminDivision()) {
                level = COUNTRY;
            } else if (name.getFeatureClass() == FeatureClass.A) {
                switch (name.getFeatureCode()) {
                    case ADM1:
                    case ADM1H:
                    case TERR:
                    case PRSH:
                        level = ADMIN1;
                        break;
                    case ADM2:
                    case ADM2H:
                        level = ADMIN2;
                        break;
                    case ADM3:
                    case ADM3H:
                        level = ADMIN3;
                        break;
                    case ADM4:
                    case ADM4H:
                        level = ADMIN4;
                        break;
                    case ADM5:
                    case ADMD:
                    case ADMDH:
                        level = ADMINX;
                        break;
                }
            } else if (name.getFeatureClass() == FeatureClass.P) {
                level = CITY;
            }
        }
        return level;
    }

    public QueryBuilder apply(final QueryBuilder builder) {
        builder.clearFeatureCodes();
        switch (this) {
            case COUNTRY:
                builder.addCountryCodes();
                break;
            case ADMIN1:
                builder.addFeatureCodes(FeatureCode.ADM1, FeatureCode.ADM1H, FeatureCode.TERR, FeatureCode.PRSH);
                break;
            case ADMIN2:
                builder.addFeatureCodes(FeatureCode.ADM2, FeatureCode.ADM2H);
                break;
            case ADMIN3:
                builder.addFeatureCodes(FeatureCode.ADM3, FeatureCode.ADM3H);
                break;
            case ADMIN4:
                builder.addFeatureCodes(FeatureCode.ADM4, FeatureCode.ADM4H);
                break;
            case ADMINX:
                builder.addFeatureCodes(FeatureCode.ADM5, FeatureCode.ADMD, FeatureCode.ADMDH);
                break;
            case CITY:
                builder.addCityCodes();
                break;
        }
        return builder;
    }

    public SearchLevel narrow() {
        switch (this) {
            case COUNTRY:
                return ADMIN1;
            case ADMIN1:
                return ADMIN2;
            case ADMIN2:
                return ADMIN3;
            case ADMIN3:
                return ADMIN4;
            case ADMIN4:
                return ADMINX;
            case ADMINX:
                return CITY;
            default:
                return null;
        }
    }

    public boolean canNarrow() {
        return narrow() != null;
    }

    public SearchLevel broaden() {
        switch (this) {
            case ADMIN1:
                return COUNTRY;
            case ADMIN2:
                return ADMIN1;
            case ADMIN3:
                return ADMIN2;
            case ADMIN4:
                return ADMIN3;
            case ADMINX:
                return ADMIN4;
            case CITY:
                return ADMINX;
            default:
                return null;
        }
    }

    public String getCode(final GeoName geoName) {
        switch (this) {
            case COUNTRY:
                return geoName.getPrimaryCountryCode().name();
            case ADMIN1:
                return geoName.getAdmin1Code();
            case ADMIN2:
                return geoName.getAdmin2Code();
            case ADMIN3:
                return geoName.getAdmin3Code();
            case ADMIN4:
                return geoName.getAdmin4Code();
            default:
                return null;
        }
    }

    public boolean isAdmin() {
        switch (this) {
            case ADMIN1:
            case ADMIN2:
            case ADMIN3:
            case ADMIN4:
            case ADMINX:
                return true;
            default:
                return false;
        }
    }
}
