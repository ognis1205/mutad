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

import java.util.List;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
final class LexemeArray {
    /** Data array; the starting position of lexemes. */
    private final List<Integer> begins;

    /** Data array; the length of lexemes. */
    private final List<Integer> lengths;

    /** Data array. */
    private final String data;

    /**
     * Instanciates from a given builder.
     * @param builder the builder to be used.
     */
    public LexemeArray(LexemeArrayBuilder builder) {
        this.begins = builder.getBegins();
        this.lengths = builder.getLengths();
        this.data = builder.getData();
    }

    /**
     * Returns the value which is specified by a given id.
     * @param id the id to be queried.
     * @return the value which is specified by a given id.
     */
    public String get(int id) {
        int begin = begins.get(id);
        int length = lengths.get(id);
        return data.substring(begin, begin + length);
    }
}
