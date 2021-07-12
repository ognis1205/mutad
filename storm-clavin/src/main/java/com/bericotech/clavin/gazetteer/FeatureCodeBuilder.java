package com.bericotech.clavin.gazetteer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;

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
 * FeatureCodeBuilder.java
 *
 *###################################################################*/

/**
 * Generates {@link FeatureClass} enum definitions from GeoNames
 * http://download.geonames.org/export/dump/featureCodes_en.txt file.
 *
 * TODO: clean this up and make it part of the install/build process
 *
 */
public class FeatureCodeBuilder {
    /**
     * Reads-in featureCodes_en.txt file, spits-out
     * {@link FeatureClass} enum definitions.
     * @param args the command line arguments
     *
     * @throws FileNotFoundException if featureCodes_en.txt is not found on the classpath
     * @throws IOException if an error occurs reading the featureCodes file
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        InputStream codeStream = FeatureCodeBuilder.class.getClassLoader().getResourceAsStream("featureCodes_en.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(codeStream, Charset.forName("UTF-8")));
        // generate dummy feature code for top-level territories
        Set<Code> featureCodes = new TreeSet<Code>();
        featureCodes.add(new Code("TERRI", "A", "independent territory", "a territory that acts as an independent political entity",
                "manually added to identify territories that can contain other administrative divisions"));
        String line;
        while ((line = in.readLine()) != null) {
            String[] tokens = line.split("\t");
            if (!tokens[0].equals("null")) {
                String[] codes = tokens[0].split("\\.");
                featureCodes.add(new Code(codes[1], codes[0], tokens[1], tokens.length == 3 ? tokens[2] : ""));
            }
        }
        in.close();
        for (Code code : featureCodes) {
            System.out.println(code);
        }
        System.out.println("// manually added for locations not assigned to a feature code");
        System.out.println("NULL(FeatureClass.NULL, \"not available\", \"\", false);");
    }

    private static class Code implements Comparable<Code> {
        public final String code;
        public final String classCode;
        public final String shortDesc;
        public final String longDesc;
        public final String comment;

        public Code(String code, String classCode, String shortDesc, String longDesc) {
            this(code, classCode, shortDesc, longDesc, null);
        }

        public Code(String code, String classCode, String shortDesc, String longDesc, String comment) {
            this.code = code;
            this.classCode = classCode;
            this.shortDesc = shortDesc;
            this.longDesc = longDesc;
            this.comment = comment;
        }

        public boolean isHistorical() {
            return shortDesc.toLowerCase().startsWith("historical") && code.toUpperCase().endsWith("H");
        }

        @Override
        public int compareTo(Code o) {
            // sort NULL code last
            if (code.equals("NULL")) {
                return code.equals(o.code) ? 0 : 1;
            }
            if (o.code.equals("NULL")) {
                return -1;
            }
            // sort by classCode, then code
            int comp = classCode.compareTo(o.classCode);
            return comp == 0 ? code.compareTo(o.code) : comp;
        }

        @Override
        public String toString() {
            String commentStr = comment != null && !comment.trim().isEmpty() ? String.format(" // %s", comment.trim()) : "";
            return String.format("%s(FeatureClass.%s, \"%s\", \"%s\", %s),%s", code, classCode, shortDesc, longDesc, isHistorical(),
                    commentStr);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.code != null ? this.code.hashCode() : 0);
            hash = 29 * hash + (this.classCode != null ? this.classCode.hashCode() : 0);
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
            final Code other = (Code) obj;
            if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
                return false;
            }
            if ((this.classCode == null) ? (other.classCode != null) : !this.classCode.equals(other.classCode)) {
                return false;
            }
            return true;
        }
    }
}
