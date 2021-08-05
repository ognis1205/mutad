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
import * as Geo from "./geo.d";
import * as Types from "./types";

export const request = (
  from: number,
  to: number,
  text: string,
  hashtags: string,
  dispatch: React.Dispatch<React.ReducerAction<any>>) => {
    const query = {
      text: text,
      hashtags: /\S/.test(hashtags) ? hashtags.split(/\s+/) : [],
      from: from,
      to: to,
    } as Geo.Query;

    const opts = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(query),
    };

    fetch(`${process.env.API_ENDPOINT}/geo/hashtags`, opts)
    .then((res) => {
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      return res.json();
    })
    .then((json) => {
      dispatch(newQuery(query));
      dispatch(newPoints(json))
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

export const newQuery = (query: Geo.Query) => ({
  type: Types.NEW_QUERY,
  payload: query,
});

export const clrQuery = () => ({
  type: Types.CLR_QUERY,
  payload: "",
});

export const newConfig = (query: Geo.Config) => ({
  type: Types.NEW_CONFIG,
  payload: query,
});

export const clrConfig = () => ({
  type: Types.CLR_CONFIG,
  payload: "",
});

export const newPoints = (json: Geo.Response[]) => ({
  type: Types.NEW_POINTS,
  payload: json,
});

export const clrPoints = () => ({
  type: Types.CLR_POINTS,
  payload: [],
});
