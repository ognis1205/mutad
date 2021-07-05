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
import java.util.BitSet;
import java.util.List;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
final class DoubleArrayAllocator {
    /** Linked node data structure used for a memory allocation. */
    private static class LinkedNode {
        public int prev;
        public int next;
        public LinkedNode(int prev, int next) {
            this.prev = prev;
            this.next = next;
        }
    }

    /** Memory allocation ratio. */
    private static final int ALLOC_RATIO = 2;

    /** Identifier number for sentinels. */
    private static final int SENTINEL = 0;

    /** Maintainer of memory address allocation. */
    private ArrayList<LinkedNode> linkedNodes = new ArrayList<LinkedNode>();

    /** Bit set if a given memory is in use. */
    private BitSet bitSet = new BitSet();

    /**
     * Resizes the allocated memory.
     * @param hint the hint to the size of memory to be allocated.
     */
    private void resize(int hint) {
        final int newSize = Math.max(hint, this.linkedNodes.size() * DoubleArrayAllocator.ALLOC_RATIO);
        if (this.linkedNodes.size() == 0) {
            this.linkedNodes.add(new DoubleArrayAllocator.LinkedNode(-1, 1));
        }
        this.linkedNodes.get(this.linkedNodes.size() - 1).next = this.linkedNodes.size();
        for (int i = this.linkedNodes.size(); i < newSize; i++) {
            this.linkedNodes.add(new DoubleArrayAllocator.LinkedNode(i - 1, i + 1));
        }
        this.linkedNodes.get(newSize - 1).next = DoubleArrayAllocator.SENTINEL;
    }

    /**
     * Assigns a memory for a given number of address.
     * @param node the address to be used.
     */
    private void assign(int node) {
        while (node >= this.linkedNodes.size() - 1) {
            this.resize(0);
        }
        this.linkedNodes.get(this.linkedNodes.get(node).prev).next = this.linkedNodes.get(node).next;
        this.linkedNodes.get(this.linkedNodes.get(node).next).prev = this.linkedNodes.get(node).prev;
        this.linkedNodes.get(node).next = DoubleArrayAllocator.SENTINEL;
    }

    /**
     * Returns true if a given string is not already registered in an double array.
     * @param codes the list of characters of a given string.
     * @param candidate the candidate index of a double array where a given string to be stored.
     * @return `true` if a given string is assignable at a specified candidate position.
     */
    private boolean isAssignable(List<Character> codes, int candidate) {
        for (Character c : codes) {
            if (candidate + c < this.linkedNodes.size()
                    && this.linkedNodes.get(candidate + c).next == DoubleArrayAllocator.SENTINEL) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an assignable address.
     * @param codes The string to be stored in character list form.
     * @return the assignable address.
     */
    public int xCheck(List<Character> codes) {
        if (this.linkedNodes.size() < Constants.DoubleArrayCheck.LIMIT_CODE) {
            this.resize(Constants.DoubleArrayCheck.LIMIT_CODE * 2);
        }
        for (int curr = this.linkedNodes.get(Constants.DoubleArrayCheck.LIMIT_CODE).next; ; curr = this.linkedNodes.get(curr).next) {
            final int candidate = curr - codes.get(0);
            if (this.bitSet.get(candidate) == false && this.isAssignable(codes, candidate)) {
                this.bitSet.flip(candidate);
                for (Character c : codes) {
                    this.assign(candidate + c);
                }
                return candidate;
            }
        }
    }
}
