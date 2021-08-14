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
import * as Action from "../../action.d";
import * as Topic from "./topic.d";

export type State = {
  loading: boolean;
  from: number;
  to: number;
  topN: number;
  topic: Topic.Model;
};

export const initState = {
  loading: false,
  from: 0,
  to: 0,
  topN: 10,
  topic: null,
} as State;

export const onLoad = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    loading: true,
  };
};

export const onDone = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    loading: false,
  };
};

export const onNewQuery = (state: State, action: Action.WithPayload<Topic.Query>) => {
  return {
    ...state,
    from: action.payload.from,
    to: action.payload.to,
  };
};

export const onClrQuery = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    from: 0,
    to: 0,
  };
};

const backgroundPalette = [
  "rgba(  0,  71, 171, 0.7)",
  "rgba( 17,  81, 171, 0.7)",
  "rgba( 34,  91, 171, 0.7)",
  "rgba( 51, 101, 171, 0.7)",
  "rgba( 68, 111, 171, 0.7)",
  "rgba( 85, 121, 171, 0.7)",
  "rgba(103, 131, 171, 0.7)",
  "rgba(120, 141, 171, 0.7)",
  "rgba(137, 151, 171, 0.7)",
  "rgba(154, 161, 171, 0.7)",
];

const borderPalette = [
  "rgb(  0,  71, 171)",
  "rgb( 17,  81, 171)",
  "rgb( 34,  91, 171)",
  "rgb( 51, 101, 171)",
  "rgb( 68, 111, 171)",
  "rgb( 85, 121, 171)",
  "rgb(103, 131, 171)",
  "rgb(120, 141, 171)",
  "rgb(137, 151, 171)",
  "rgb(154, 161, 171)",
];

export const onNewTopic = (state: State, action: Action.WithPayload<Topic.Response[]>) => {
  return {
    ...state,
    topic: {
      labels: action.payload.map((e) => "#" + e.name),
      datasets: [{
        label: "count",
        data: action.payload.map((e) => e.count),
        backgroundColor: backgroundPalette,
        borderColor: borderPalette,
      }],
    },
  };
};

export const onClrTopic = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    topic: null,
  };
};
