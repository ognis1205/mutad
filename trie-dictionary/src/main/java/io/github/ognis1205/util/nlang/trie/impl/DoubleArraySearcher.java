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
package io.github.ognis1205.util.nlang.trie.impl;

import io.github.ognis1205.util.nlang.trie.TrieSearcher;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public final class DoubleArraySearcher implements TrieSearcher {
    /** Double Array to be searched. */
    private final DoubleArray doubleArray;

    /**
     * Instanciates from a given Double Array.
     * @param doubleArray the double array to be set.
     */
    public DoubleArraySearcher(DoubleArray doubleArray) {
        this.doubleArray = doubleArray;
    }

    /**
     * Returns the size of a double array.
     * @return the size of a double array.
     */
    public int size() {
        return this.doubleArray.keySetSize;
    }

    /**
     * Returns `true` if the trailing string after a specified node in a double array
     * matches to a given key.
     * @param key the key to be checked.
     * @param node the node number where match starts.
     */
    private boolean matchTail(StringStream key, int node) {
        final int id = Constants.DoubleArrayBase.ID(node);
        final int begin = this.doubleArray.begins.get(id);
        final int offset = this.doubleArray.lengths.get(id);
        final String suffix = this.doubleArray.tail.substring(begin, begin + offset);
        return key.rest(-1).equals(suffix);
    }

    /**
     * Calls a given callback function when the prefix of a given key matches to a
     * registered element in a double array.
     * @param key the key to be checked.
     * @param node the current position of a double array.
     * @param begin the current position of a given key.
     * @param offset the relative position of a match from a given begin position.
     * @param func the callback function.
     */
    private void applyIfKeyIncludesSuffix(StringStream key, int node, int begin, int offset, Callback func) {
        final int id = Constants.DoubleArrayBase.ID(node);
        final int suffixBegin = this.doubleArray.begins.get(id);
        final int suffixOffset = this.doubleArray.lengths.get(id);
        if (key.startsWith(this.doubleArray.tail, suffixBegin, suffixOffset)) {
            func.apply(begin, offset, id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int membership(CharSequence key) {
        int node = this.doubleArray.base.get(0);
        StringStream keyStream = new StringStream(key);
        for (char code = keyStream.read(); ; code = keyStream.read()) {
            final int index = node + code;
            if (index < 0) return -1;
            node = this.doubleArray.base.get(index);
            if (this.doubleArray.check.get(index) == code) {
                if (node >= 0) {
                    continue;
                } else if (keyStream.eos() || this.matchTail(keyStream, node)) {
                    return Constants.DoubleArrayBase.ID(node);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eachCommonPrefix(CharSequence query, int begin, Callback func) {
        int node = this.doubleArray.base.get(0);
        int offset = 0;
        StringStream queryStream = new StringStream(query, begin);
        for (char code = queryStream.read(); ; code = queryStream.read(), offset++) {
            final int index = node + code;
            offset++;
            if (index < 0) return;
            node = this.doubleArray.base.get(index);
            if (this.doubleArray.check.get(index) == code) {
                if (node >= 0) {
                    continue;
                } else {
                    this.applyIfKeyIncludesSuffix(queryStream, node, begin, offset, func);
                }
            } else {
                return;
            }
        }
    }
}
