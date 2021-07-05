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

import java.util.ArrayList;
import java.util.List;
import io.github.ognis1205.util.nlang.trie.Trie;
import io.github.ognis1205.util.nlang.trie.TrieBuilder;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
final class LexemeArrayBuilder<T> implements TrieBuilder.Callback {
    /** Lexemes to be used to build an array. */
    private final List<? extends Lexeme<T>> lexemes;

    /** Tail array. */
    private final List<T> data = new ArrayList<T>();

    /**
     * Instanciates from a given Lexeme Array builder.
     * @param builder the builder to be used to construct a Lexeme Array.
     */
    public static <T> LexemeArray<T> build(LexemeArrayBuilder<T> builder) {
        return new LexemeArray<T>(builder);
    }

    /**
     * Instanciates from a given lexemes.
     * @param lexemes the lexemes to be used to build an array.
     */
    public LexemeArrayBuilder(List<? extends Lexeme<T>> lexemes, boolean sorted) {
//        if (!sorted) {
            java.util.Collections.sort(lexemes);
//        }
        this.lexemes = lexemes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(int id) {
        Lexeme<T> lexeme = this.lexemes.get(id);
//        if (lexeme != null) {
//            System.out.println(lexeme.getKey());
//            if (lexeme.getKey().equals("Tokyo")) {
//                System.out.println("APPLY");
//                System.out.println(id);
//                System.out.println(lexeme.getKey());
//                System.out.println("APPLY END");
//            }
            T value = lexeme.getValue();
            this.data.add(id, value);
//        }
    }

    public List<? extends Trie.Entry<T>> getTrieEntryList() {
        return this.lexemes;
    }

    public List<T> getData() {
        return this.data;
    }
}
