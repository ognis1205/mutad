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
package io.github.ognis1205.tweet_visualization.data_collectors;

import io.github.ognis1205.tweet_visualization.data_collectors.impl.TweetCollector;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class DataCollectionDriver {
    public static void main(String[] args) {
        Collector<String> collect = new TweetCollector(
                System.getenv("HOSEBIRD_API_KEY"),
                System.getenv("HOSEBIRD_API_SECRET"),
                System.getenv("HOSEBIRD_TOKEN"),
                System.getenv("HOSEBIRD_TOKEN_SECRET")
        );
        collect.start();
        while (true) {
            System.out.println(collect.collect());
        }
        //collect.stop();
    }
}
