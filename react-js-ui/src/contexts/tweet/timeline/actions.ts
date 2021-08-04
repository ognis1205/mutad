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

export const newTimeline = (dispatch: React.Dispatch<React.ReducerAction<any>>) => {
  const query = {
    before: new Date().getTime(),
    text: "",
    hashtags: [],
    page: 0,
    size: 50,
  } as Tweet.Query;

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
    dispatch(newQuery(query));
    dispatch(newLatestTweets(json))
    dispatch(asyncDone())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return asyncLoad();
};

export const refTimeline = (text: string, hashtags: string, dispatch: React.Dispatch<React.ReducerAction<any>>) => {
  const query = {
    before: new Date().getTime(),
    text: text,
    hashtags: /\S/.test(hashtags) ? hashtags.split(/\s+/) : [],
    page: 0,
    size: 50,
  } as Tweet.Query;

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
    dispatch(newQuery(query));
    dispatch(newLatestTweets(json))
    dispatch(asyncDone())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return asyncLoad();
};

export const addTimeline = (text: string, hashtags: string, page: number, dispatch: React.Dispatch<React.ReducerAction<any>>) => {
  const query = {
    before: new Date().getTime(),
    text: text,
    hashtags: /\S/.test(hashtags) ? hashtags.split(/\s+/) : [],
    page: page + 1,
    size: 50,
  } as Tweet.Query;

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
    dispatch(newQuery(query));
    dispatch(addLatestTweets(json))
    dispatch(asyncDone())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return asyncLoad();
};

const asyncLoad = () => ({
  type: Types.ASYNC_LOAD,
  payload: true,
});

const asyncDone = () => ({
  type: Types.ASYNC_DONE,
  payload: false,
});

export const newQuery = (query: Tweet.Query) => ({
  type: Types.NEW_QUERY,
  payload: query,
});

export const clrQuery = () => ({
  type: Types.NEW_QUERY,
  payload: "",
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
