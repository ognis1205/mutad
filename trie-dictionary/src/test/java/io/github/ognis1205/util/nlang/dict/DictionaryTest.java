/*
 * Copyright 2021 Shingo OKAWA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ognis1205.util.nlang.dict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class DictionaryTest {
    private static class Coord {
        public double lon;

        public double lat;

        public Coord(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }
    }

    private static class CSVEntry extends Lexeme<List<Coord>> {
        private String key;

        private List<Coord> value;

        public CSVEntry (List<String> line) {
            this.key = line.get(0);
            this.value = new ArrayList<>();
            this.value.add(new Coord(Double.parseDouble(line.get(1)), Double.parseDouble(line.get(2))));
        }

        @Override
        public String getKey() { return this.key; }

        @Override
        public List<Coord> getValue() { return this.value; }

        public void add(Coord coord) {
            this.value.add(coord);
        }
    }

    private static Map<String, CSVEntry> parseCsv(Path path) throws IOException {
        Map<String, CSVEntry> result = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s*,\\s*");
                if (result.containsKey(values[0])) {
                    result.get(values[0]).add(new Coord(Double.parseDouble(values[1]), Double.parseDouble(values[2])));
                } else {
                    result.put(values[0], new CSVEntry(Arrays.asList(values)));
                }
            }
        }
        return result;
    }

    private static List<CSVEntry> CSV;

    private Dictionary<List<Coord>> dict;

    @BeforeAll
    static void beforeAll() {
        try {
            CSV = new ArrayList<>(parseCsv(Paths.get("src", "test", "resources", "worldcities.csv")).values());
        } catch (Exception e) {
            System.err.println("failed to load CSV file.");
        };
        Assumptions.assumeTrue(CSV != null);
    }

    @BeforeEach
    void beforeEach() {
        this.dict = new Dictionary<List<Coord>>(CSV, false);
    }

    @Test
    void testMembership() {
        assertEquals(this.dict.get(this.dict.membership("Tokyo")).get(0).lat, 139.7514);
        assertEquals(this.dict.get(this.dict.membership("Tokyo")).get(0).lon, 35.685);
        assertEquals(this.dict.get(this.dict.membership("Tokushima")).get(0).lat, 134.5525);
        assertEquals(this.dict.get(this.dict.membership("Tokushima")).get(0).lon, 34.0674);
    }

    @Test
    void testPrefix() {
        // Do nothing.
    }
}
