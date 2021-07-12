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
 * MultipartLocationName.java
 *
 *###################################################################*/

package com.bericotech.clavin.resolver.multipart;

/**
 * Convenience object used as input for {@link MultipartLocationResolver}
 *
 * Container class representing a multipart location name, such as
 * what's often found in structured data like a spreadsheet or database
 * table, e.g., [Reston][Virginia][United States]. The names are stored
 * as three Strings.
 *
 */
public class MultipartLocationName {

    /**
     * The city, state/province/etc., and country names making up a
     * multipart location name, e.g., [Reston][Virginia][United States]
     */
    private final String city;
    private final String state;
    private final String country;

    /**
     * Sole constructor for {@link MultipartLocationName} class.
     *
     * Represents a multipart location name, such as what's often found
     * in structured data, e.g., [Reston][Virginia][United States].
     *
     * @param city      the text of the city name, e.g., "Reston"
     * @param state     the text of the city/province/etc. name, e.g., "Virginia"
     * @param country   the text of the country name, e.g., "United States"
     */
    public MultipartLocationName(String city, String state, String country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }

    /**
     * Tests equivalence based on names of city, state, and country.
     * @param obj   Object to compare this against
     * @return      true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;

        if (this.getClass() != obj.getClass()) return false;
        MultipartLocationName other = (MultipartLocationName)obj;
        return this.getCity().equalsIgnoreCase(other.getCity())
                && this.getState().equalsIgnoreCase(other.getState())
                && this.getCountry().equalsIgnoreCase(other.getCountry());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.city != null ? this.city.hashCode() : 0);
        hash = 71 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 71 * hash + (this.country != null ? this.country.hashCode() : 0);
        return hash;
    }

    /**
     * For pretty-printing.
     * @return  "(city, state, country)"
     */
    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", getCity(), getState(), getCountry());
    }

    /**
     * Get the text of the city name.
     * @return  the text of the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the text of the state/province/etc. name.
     * @return  the text of the state/province/etc. name
     */
    public String getState() {
        return state;
    }

    /**
     * Get the text of the country name.
     * @return  the text of the country name
     */
    public String getCountry() {
        return country;
    }
}
