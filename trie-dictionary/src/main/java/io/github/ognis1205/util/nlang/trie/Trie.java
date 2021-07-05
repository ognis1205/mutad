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

import io.github.ognis1205.util.nlang.trie.impl.DoubleArraySearcher;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public interface Trie {
    /** Interface for Double Array elements. */
    public static abstract class Entry<T> implements Comparable<Entry> {
        public abstract String getKey();

        public abstract T getValue();

        @Override
        public int compareTo(Entry that) {
            return this.getKey().compareTo(that.getKey());
        }
    }

    /**
     * Returns true if a given key is registered in a TRIE tree.
     * @param key the key to be checked.
     * @return the id if a given key is already registered, otherwise -1.
     */
    public int membership(CharSequence key);

    /**
     * Common-prefix search API.
     * @param query the query to be requested.
     * @param begin the begin position within a given query.
     * @param func the callback function.
     */
    public void prefix(CharSequence query, int begin, DoubleArraySearcher.Callback func);
}
