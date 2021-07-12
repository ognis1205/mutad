package com.bericotech.clavin.index;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

/*#####################################################################
 * 
 * CLAVIN (Cartographic Location And Vicinity INdexer)
 * ---------------------------------------------------
 * 
 * Copyright (C) 2012-2013 Berico Technologies
 * http://clavin.bericotechnologies.com
 * 
 * ====================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * ====================================================================
 * 
 * WhitespaceLowerCaseAnalyzer.java
 * 
 *###################################################################*/

/**
 * A Lucene Analyzer that filters WhitespaceTokenizer with
 * LowerCaseFilter.
 * 
 */
public class WhitespaceLowerCaseAnalyzer extends Analyzer {
    /**
     * Simple default constructor for
     * {@link WhitespaceLowerCaseAnalyzer}.
     * 
     */
    public WhitespaceLowerCaseAnalyzer() {}

    /**
     * Provides tokenizer access for the analyzer.
     * 
     * @param fieldName     field to be tokenized
     * @return              returns {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
     */
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new WhitespaceLowerCaseTokenizer());
    }
}
