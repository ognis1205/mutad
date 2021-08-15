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
import * as Topic from "./topic.d";
import * as Timeseries from "./timeseries.d";
import * as Types from "./types";

export const requestTopics = (dispatch: React.Dispatch<React.ReducerAction<any>>) => {
  const to = new Date();
  const from = new Date();
  from.setMonth(to.getMonth() - 2);
  const query = {
    from: from.getTime(),
    to: to.getTime(),
    topN: 10,
  } as Topic.Query;

  const opts = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(query),
  };

  fetch(`${process.env.API_ENDPOINT}/tweet/topics`, opts)
  .then((res) => {
    if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
    return res.json();
  })
  .then((json) => {
    dispatch(newTopic(json))
    dispatch(done())
  })
  .catch((reason) => {
    console.log(reason);
  });

  return load();
};

export const requestTimeseries = (dispatch: React.Dispatch<React.ReducerAction<any>>) => {
  const to = new Date("2021-07-26T02:00:00.000+00:00");
  const from = new Date("2021-07-26T00:00:00.000+00:00");
//  from.setMonth(to.getMonth() - 2);
  const query = {
    from: from.getTime(),
    to: to.getTime(),
    interval: "1m",
  } as Topic.Query;

  const opts = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(query),
  };

  fetch(`${process.env.API_ENDPOINT}/tweet/counts`, opts)
  .then((res) => {
    if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
    return res.json();
  })
  .then((json) => {
    dispatch(newTimeseries(json))
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

export const newTopic = (json: Topic.Response[]) => ({
  type: Types.NEW_TOPIC,
  payload: json,
});

export const clrTopic = () => ({
  type: Types.CLR_TOPIC,
  payload: [],
});

export const newTimeseries = (json: Timeseries.Response[]) => ({
  type: Types.NEW_TIMESERIES,
  payload: json,
});

export const addTimeseries = (json: Timeseries.Response[]) => ({
  type: Types.ADD_TIMESERIES,
  payload: json,
});

export const clrTimeseries = () => ({
  type: Types.CLR_TIMESERIES,
  payload: [],
});
