package com.bericotech.clavin.extractor;

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
 * LocationOccurrence.java
 *
 *###################################################################*/

/**
 * Container class representing a location name found in a document.
 * Stores the text of the location name itself, as well as its position
 * in the text in which it was found (measured in UTF-16 code points
 * from the start of the document).
 *
 */
public class LocationOccurrence {
    // text representation of the location (i.e., its name)
    private final String text;

    // number of UTF-16 code units from the start of the document at
    // which the location name starts
    private final int position;

    /**
     * Sole construction for {@link LocationOccurrence} class.
     *
     * Represents a location name found in a document.
     *
     * @param text      text of the location name
     * @param position  where it was found
     */
    public LocationOccurrence(String text, int position) {
        this.text = text;
        this.position = position;
    }

    /**
     * Get the text of the location name.
     * @return the text of the location name
     */
    public String getText() {
        return text;
    }

    /**
     * Get the position in the text where the location name starts.
     * @return the number of UTF-16 code units from the start of the
     * document at which the location name starts
     */
    public int getPosition() {
        return position;
    }

    /**
     * Tests equivalence based on name and position.
     *
     * @param o     Object to compare this against
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationOccurrence that = (LocationOccurrence) o;

        if (position != that.position) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    /**
     * Required for hashing.
     */
    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + position;
        return result;
    }

    @Override
    public String toString() {
        return String.format("\"%s\":%d", text, position);
    }
}