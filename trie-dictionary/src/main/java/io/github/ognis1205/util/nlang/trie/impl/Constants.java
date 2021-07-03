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
final class Constants {
    /** Constants relating Double Array Base. */
    public static class DoubleArrayBase {
        /** Initial value for a base node. */
        public static final int INIT_VALUE = Integer.MIN_VALUE;

        /** Self-inverse mapping for ids. */
        public static int ID(int id) {
            return id * -1 - 1;
        }
    }

    /** Constants relating Double Array Check. */
    public static class DoubleArrayCheck {
        /** Character code representing the end of a keyword. */
        public static final char TERM_CODE = 0;

        /** Character code for an empty node. */
        public static final char EMPTY_CODE = 1;

        /** Maximum value of character codes. */
        public static final char LIMIT_CODE = 0xFFFF;
    }
}
