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
import React from "react";
import * as Tweet from "./tweet.d";
import * as Types from "./types";

export const reqLatestTweets = (query: Tweet.Query) => async (dispatch: React.Dispatch<React.ReducerAction<any>>) => {
    const opts = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(query),
    };
    fetch(`${process.env.API_ENDPOINT}/tweet/latest`, opts)
    .then((res) => {
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      return res.json();
    })
    .then((json) => {
      dispatch(newLatestTweets(json))
    })
    .catch((reason) => {
      console.log(reason);
    });
};

export const updLatestTweets = (query: Tweet.Query) => async (dispatch: React.Dispatch<React.ReducerAction<any>>) => {
    const opts = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(query),
    };
    fetch(`${process.env.API_ENDPOINT}/tweet/latest`, opts)
    .then((res) => {
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      return res.json();
    })
    .then((json) => {
      dispatch(addLatestTweets(json))
    })
    .catch((reason) => {
      console.log(reason);
    });
};

export const newText = (text: string) => ({
  type: Types.NEW_TEXT,
  payload: text,
});

export const clrText = () => ({
  type: Types.CLR_TEXT,
  payload: "",
});

export const newHashtags = (hashtags: string) => ({
  type: Types.NEW_HASHTAGS,
  payload: hashtags,
});

export const clrHashtags = () => ({
  type: Types.CLR_HASHTAGS,
  payload: "",
});

export const newTimestamp = (timestamp: number) => ({
  type: Types.NEW_TIMESTAMP,
  payload: timestamp,
});

export const clrTimestamp = () => ({
  type: Types.CLR_TIMESTAMP,
  payload: 0,
});

export const newPage = (page: number) => ({
  type: Types.NEW_PAGE,
  payload: page,
});

export const clrPage = () => ({
  type: Types.CLR_PAGE,
  payload: 0,
});

export const opnDialog = () => ({
  type: Types.OPN_DIALOG,
  payload: true,
});

export const clsDialog = () => ({
  type: Types.CLS_DIALOG,
  payload: false,
});

export const newLatestTweets = (json: Tweet.Response[]) => ({
  type: Types.NEW_LATEST_TWEETS,
  payload: json,
});

export const addLatestTweets = (json: Tweet.Response[]) => ({
  type: Types.ADD_LATEST_TWEETS,
  payload: json,
});

export const clrLatestTweets = () => ({
  type: Types.CLR_LATEST_TWEETS,
  payload: [],
});
