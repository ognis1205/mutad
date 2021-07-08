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
package io.github.ognis1205.tweet_visualization.storm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.vdurmont.emoji.EmojiParser;

public class Texts {
    /** Regex pattern for URLs. */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
            Pattern.CASE_INSENSITIVE);

    /**
     * Remove all appearances of URLs from the given string.
     * @param str string to be cleaned.
     * @return URLs removed string.
     */
    public static String removeUrl(String str) {
        Matcher match = URL_PATTERN.matcher(str);
        int i = 0;
        while (match.find()) {
            str = str.replaceAll(match.group(i++),"").trim();
        }
        return str;
    }

    /**
     * Remove all appearances of emojis from the given string.
     * @param str string to be cleaned.
     * @return emoji removed string.
     */
    public static String removeEmoji(String str) {
        return EmojiParser.removeAllEmojis(str);
    }

    /** Constructor. */
    private Texts() {}
}
