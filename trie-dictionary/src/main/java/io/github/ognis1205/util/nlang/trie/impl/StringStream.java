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

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
final class StringStream implements Comparable<StringStream> {
    /** Actual data. */
    private final CharSequence sequence;

    /** Index referring to the current position. */
    private int curr;

    /**
     * Instanciates StringStream from a given data.
     * @param sequence the data.
     */
    public StringStream(CharSequence sequence) {
        this.sequence = sequence;
        this.curr = 0;
    }

    /**
     * Instanciates StringStream from a given data with a given index.
     * @param sequence the data.
     * @param index the index referring to the current position.
     */
    public StringStream(CharSequence sequence, int index) {
        this.sequence = sequence;
        this.curr = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.sequence.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(StringStream stream) {
        return this.rest().compareTo(stream.rest());
    }

    /**
     * Returns `true` if the data starts with a given prefix.
     * @param prefix the prefix to be checked.
     * @param begin the relative position within a given prefix where the comparison starts.
     * @param length the length of a given prefix.
     */
    public boolean startsWith(CharSequence prefix, int begin, int length) {
        if (this.sequence.length() - this.curr < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (this.sequence.charAt(this.curr + i) != prefix.charAt(begin + i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the trailing string after the current position.
     * @return the rest of the string.
     */
    public String rest() {
        return this.sequence.subSequence(this.curr, this.sequence.length()).toString();
    }

    /**
     * Returns the trailing string after the current position added with a given offset.
     * @param offset the offset to be added.
     * @return the rest of the string.
     */
    public String rest(int offset) {
        int begin = this.curr + offset;
        if (0 <= begin && begin < this.sequence.length()) {
            return this.sequence.subSequence(begin, this.sequence.length()).toString();
        }
        return this.rest();
    }

    /**
     * Returns a character at the current position.
     * @return the current character.
     */
    public char read() {
        return this.eos() ? Constants.DoubleArrayCheck.TERM_CODE : this.sequence.charAt(this.curr++);
    }

    /**
     * Returns `true` if the current position is at the end of a string.
     * @return `true` if the current position is at the end of a string.
     */
    public boolean eos() {
        return this.curr == this.sequence.length();
    }
}
