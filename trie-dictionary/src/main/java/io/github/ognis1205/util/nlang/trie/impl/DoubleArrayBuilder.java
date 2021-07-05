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

import java.util.ArrayList;
import java.util.List;
import io.github.ognis1205.util.nlang.trie.Trie;
import io.github.ognis1205.util.nlang.trie.TrieBuilder;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public final class DoubleArrayBuilder implements TrieBuilder {
    /** List of keywords. */
    private final List<StringStream> keys;

    /** Base array. */
    private final DynamicArrayList<Integer> base = new DynamicArrayList<Integer>();

    /** Check array. */
    private final DynamicArrayList<Character> check = new DynamicArrayList<Character>();

    /** Tail array; starting positions of postfixes. */
    private final ArrayList<Integer> begins = new ArrayList<Integer>();

    /** Tail array; lengths of postfixes. */
    private final ArrayList<Integer> lengths = new ArrayList<Integer>();

    /** Tail array. */
    private final StringBuilder tail = new StringBuilder();

    /**
     * Instanciates from a given Double Array builder and keywords.
     * @param keys the keywords to be registered.
     * @param sorted if this value is set to be `true`, treat a given keywords as already sorted.
     */
    public static DoubleArray build(List<? extends Trie.Entry> keys, boolean sorted, Callback func) {
        DoubleArrayBuilder builder = new DoubleArrayBuilder(keys, sorted);
        builder.build(new DoubleArrayAllocator(), 0, builder.keys.size(), 0, func);
        return new DoubleArray(builder);
    }

    /**
     * Instanciates from a given Double Array builder and keywords.
     * @param keys the keywords to be registered.
     * @param sorted if this value is set to be `true`, treat a given keywords as already sorted.
     */
    private DoubleArrayBuilder(List<? extends Trie.Entry> keys, boolean sorted) {
        this.keys = new ArrayList<StringStream>(keys.size());
        if (!sorted) {
            java.util.Collections.sort(keys);
        }
        CharSequence prev = null;
        CharSequence keyString = null;
        for (Trie.Entry key : keys) {
            keyString = key.getKey();
            if (!keyString.equals(prev)) {
                this.keys.add(new StringStream(prev = keyString));
            }
        }
    }

    /**
     * Builds Double Array.
     * @param allocator the memory allocator.
     * @param begin the start position of a keyword.
     * @param end the end position of a keyword.
     * @param rootIndex the index of a root node.
     * @param func the callback function which will be called after a given keyword is actually registered.
     */
    private void build(DoubleArrayAllocator allocator, int begin, int end, int rootIndex, Callback func) {
        if (end - begin == 1) {
            this.insertTail(this.keys.get(begin), rootIndex, func);
            return;
        }

        final List<Integer> ends = new ArrayList<Integer>();
        final List<Character> codes = new ArrayList<Character>();
        char prev = Constants.DoubleArrayCheck.EMPTY_CODE;

        for (int i = begin; i < end; i++) {
            char curr = keys.get(i).read();
            if (prev != curr) {
                codes.add(prev = curr);
                ends.add(i);
            }
        }
        ends.add(end);

        final int xNode = allocator.xCheck(codes);
        for (int i = 0; i< codes.size(); i++) {
            this.build(allocator, ends.get(i), ends.get(i + 1), this.setNode(codes.get(i), rootIndex, xNode), func);
        }
    }

    /**
     * Sets a given node into a given index.
     * @param code the character code.
     * @param parentIndex the index of the parent node of a given code.
     * @param xNode the node number which is specified by xNode algorithm.
     * @return the node number of a newly registered character.
     */
    private int setNode(char code, int parentIndex, int xNode) {
        final int childNode = xNode + code;
        this.base.set(parentIndex, xNode, Constants.DoubleArrayBase.INIT_VALUE);
        this.check.set(childNode, code, Constants.DoubleArrayCheck.EMPTY_CODE);
        return childNode;
    }

    /**
     * Inserts a given postfix into a Tail array;
     * @param key the key to be inserted
     * @param nodeIndex the nodeIndex of the key.
     * @param func the callback function.
     */
    private void insertTail(StringStream key, int nodeIndex, Callback func) {
        String suffix = key.rest(-1);
        int id = Constants.DoubleArrayBase.ID(this.begins.size());
        //System.out.println(Constants.DoubleArrayBase.ID(id));
        this.base.set(nodeIndex, id, Constants.DoubleArrayBase.INIT_VALUE);
        this.begins.add(this.tail.length());
        this.lengths.add(suffix.length());
        this.tail.append(suffix);
        func.apply(Constants.DoubleArrayBase.ID(id));
    }

    public int getKeySetSize() {
        return this.keys.size();
    }

    public DynamicArrayList<Integer> getBase() {
        return this.base;
    }

    public DynamicArrayList<Character> getCheck() {
        return this.check;
    }

    public List<Integer> getBegins() {
        return this.begins;
    }

    public List<Integer> getLengths() {
        return this.lengths;
    }

    public StringBuilder getTail() {
        return this.tail;
    }
}
