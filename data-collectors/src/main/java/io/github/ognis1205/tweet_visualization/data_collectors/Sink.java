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

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public interface Sink<T> {
    /**
     * Connects to a persistent layer for the middle/long-term data storage.
     * This method is supposed to be called before calling `send` method, responsible
     * for ensuring the pre-process is executed properly before storing data.
     */
    void connect();

    /**
     * Sends data to a specified persistent data layer.
     */
    void send(T data);

    /**
     * Closes the connection to a persistent data layer.
     * This method is supposed to be called after the data collection loop ends, responsible
     * for ensuring the post-process is executed properly after storing data.
     */
    void close();
}