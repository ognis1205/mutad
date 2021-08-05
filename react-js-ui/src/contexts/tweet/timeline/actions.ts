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

export const init = (dispatch: React.Dispatch<React.ReducerAction<any>>) => {
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
    dispatch(newLatest(json))
    dispatch(done())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return load();
};

export const refresh = (text: string, hashtags: string, dispatch: React.Dispatch<React.ReducerAction<any>>) => {
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
    dispatch(newLatest(json))
    dispatch(done())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return load();
};

export const more = (text: string, hashtags: string, page: number, dispatch: React.Dispatch<React.ReducerAction<any>>) => {
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
    dispatch(addLatest(json))
    dispatch(done())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return load();
};

const load = () => ({
  type: Types.LOAD,
  payload: true,
});

const done = () => ({
  type: Types.DONE,
  payload: false,
});

export const newQuery = (query: Tweet.Query) => ({
  type: Types.NEW_QUERY,
  payload: query,
});

export const clrQuery = () => ({
  type: Types.CLR_QUERY,
  payload: "",
});

export const open = () => ({
  type: Types.OPEN,
  payload: true,
});

export const close = () => ({
  type: Types.CLOSE,
  payload: false,
});

export const newLatest = (json: Tweet.Response[]) => ({
  type: Types.NEW_LATEST,
  payload: json,
});

export const addLatest = (json: Tweet.Response[]) => ({
  type: Types.ADD_LATEST,
  payload: json,
});

export const clrLatest = () => ({
  type: Types.CLR_LATEST,
  payload: [],
});
