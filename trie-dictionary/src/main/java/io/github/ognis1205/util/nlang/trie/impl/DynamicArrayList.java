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

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
final class DynamicArrayList<E> extends ArrayList<E> {
    /**
     * Returns the element at a given index. If the given index is not defined
     * already, this function allocates memory filled with given default values
     * and returns it.
     * @param index the index specifying the position to get.
     * @param defaultValue the default value.
     * @return the value of an element at a given index.
     */
    public E get(int index, E defaultValue) {
        try {
            return super.get(index);
        } catch (IndexOutOfBoundsException ex) {
            for (int i = super.size(); i <= index * 2; i++) {
                super.add(defaultValue);
            }
            return super.get(index);
        }
    }

    /**
     * Sets the element at a given index. If the given index is not defined
     * already, this function allocates the memory area filled with given default
     * values and set a given element at the given index and returns it.
     * @param index the index specifying the position to set.
     * @param element the element to be stored.
     * @param defaultValue the default value.
     * @return the value of an element at a given position.
     */
    public E set(int index, E element, E defaultValue) {
        try {
            return super.set(index, element);
        } catch (IndexOutOfBoundsException ex) {
            for (int i = super.size(); i <= index * 2; i++) {
                super.add(defaultValue);
            }
            return super.set(index, element);
        }
    }
}