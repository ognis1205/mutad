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

import java.util.List;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public final class DoubleArray {
    /** Number of registered keys. */
    public int keySetSize;

    /** Base array. */
    public final DynamicArrayList<Integer> base;

    /** Check array. */
    public final DynamicArrayList<Character> check;

    /** Tail array; starting positions of postfixes. */
    public final List<Integer> begins;

    /** Tail array; lengths of postfixes. */
    public final List<Integer> lengths;

    /** Tail array. */
    public StringBuilder tail;

    /**
     * Instanciates from a given Double Array builder.
     * @param builder the double array builder.
     */
    public DoubleArray(DoubleArrayBuilder builder) {
        this.keySetSize = builder.getKeySetSize();
        this.base = builder.getBase();
        this.check = builder.getCheck();
        this.begins = builder.getBegins();
        this.lengths = builder.getLengths();
        this.tail = builder.getTail();
    }
}

