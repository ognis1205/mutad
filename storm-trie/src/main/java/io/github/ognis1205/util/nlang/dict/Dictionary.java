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

import java.util.List;
import io.github.ognis1205.util.nlang.trie.Trie;
import io.github.ognis1205.util.nlang.trie.TrieSearcher;
import io.github.ognis1205.util.nlang.trie.impl.DoubleArray;
import io.github.ognis1205.util.nlang.trie.impl.DoubleArrayBuilder;
import io.github.ognis1205.util.nlang.trie.impl.DoubleArraySearcher;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class Dictionary<T> implements Trie {
    /** Index for TRIE trie. */
    public DoubleArraySearcher index;

    /** Indexed lexemes. */
    public LexemeArray<T> lexemes;

    /**
     * Instanciates from a given lexemes.
     * @param lexemes the lexemes to be indexed.
     * @param sorted if this value is set to be `true,` a given lexemes will be treated as already sorted.
     */
    public Dictionary(List<? extends Lexeme<T>> lexemes, boolean sorted) {
        LexemeArrayBuilder<T> lexemeArrayBuilder = new LexemeArrayBuilder<T>(lexemes, sorted);
        DoubleArray doubleArray = DoubleArrayBuilder.build(lexemeArrayBuilder.getTrieEntryList(), sorted, lexemeArrayBuilder);
        this.lexemes = LexemeArrayBuilder.build(lexemeArrayBuilder);
        this.index = new DoubleArraySearcher(doubleArray);
    }

    /**
     * Instanciates from a given index and lexemes.
     * @param index the index of a given lexemes.
     * @param lexemes the lexemes to be indexed.
     */
    public Dictionary(DoubleArray index, LexemeArray lexemes) {
        this.index = new DoubleArraySearcher(index);
        this.lexemes = lexemes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int membership(CharSequence key) {
        return this.index.membership(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prefix(CharSequence query, int begin, TrieSearcher.Callback func) {
        this.index.eachCommonPrefix(query, begin, func);
    }

    /**
     * Returns the value of a given id.
     * @param id the id of a registered value.
     * @return the registered id.
     */
    public T get(int id) {
        return this.lexemes.get(id);
    }
}
