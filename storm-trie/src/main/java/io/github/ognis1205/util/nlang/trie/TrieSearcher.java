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
package io.github.ognis1205.util.nlang.trie;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public interface TrieSearcher {
    /**
     * Callback function called when a key is found by common prefix search.
     */
    public static interface Callback {
        /**
         * Callback function implementation.
         * @param begin the position where a search starts from within a given string.
         * @param offset the position of the end of the key when a given string is matched.
         * @param id the id of the matched key.
         */
        public void apply(int begin, int offset, int id);
    }

    /**
     * Checks if a given key is registered already.
     * @param key the key to be checked.
     * @return the id if the key is registered, otherwise -1.
     */
    public int membership(CharSequence key);

    /**
     * Common prefix search API.
     * @param query the string to be queried.
     * @param begin the position of the start of a search.
     * @param func the callback function.
     */
    public void eachCommonPrefix(CharSequence query, int begin, Callback func);
}