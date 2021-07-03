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
final class LexemeArrayBuilder implements TrieBuilder.Callback {
    /** Lexemes to be used to build an array. */
    private final List<? extends Lexeme> lexemes;

    /** Data array; the starting position of lexemes. */
    private final ArrayList<Integer> begins = new ArrayList<Integer>();

    /** Data array; the length of lexemes. */
    private final ArrayList<Integer> lengths = new ArrayList<Integer>();

    /** Tail array. */
    private final StringBuffer data = new StringBuffer();

    /**
     * Instanciates from a given Lexeme Array builder.
     * @param builder the builder to be used to construct a Lexeme Array.
     */
    public static LexemeArray build(LexemeArrayBuilder builder) {
        return new LexemeArray(builder);
    }

    /**
     * Instanciates from a given lexemes.
     * @param lexemes the lexemes to be used to build an array.
     */
    public LexemeArrayBuilder(List<? extends Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(int id) {
        Lexeme lexeme = this.lexemes.get(id);
        if (lexeme != null) {
            String value = lexeme.getValue();
            this.begins.add(data.length());
            this.lengths.add(value.length());
            this.data.append(value);
        }
    }

    public List<? extends Trie.Entry> getTrieEntryList() {
        return this.lexemes;
    }

    public List<Integer> getBegins() {
        return this.begins;
    }

    public List<Integer> getLengths() {
        return this.lengths;
    }

    public String getData() {
        return data.toString();
    }
}
