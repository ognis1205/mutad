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
import * as Geo from "./geo.d";

export type State = {
  loading: boolean;
  text: string;
  hashtags: string;
  from: number;
  to: number;
  radius: number;
  blur: number;
  zoom: number;
  points: Geo.Model[];
};

export const initState = {
  loading: false,
  text: "",
  hashtags: "",
  from: 0,
  to: 0,
  radius: 10,
  blur: 10,
  zoom: 8,
  points: [],
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

export const onNewQuery = (state: State, action: Action.WithPayload<Geo.Query>) => {
  return {
    ...state,
    text: action.payload.text,
    hashtags: action.payload.hashtags.join(""),
    from: action.payload.from,
    to: action.payload.to,
  };
};

export const onClrQuery = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    text: "",
    hashtags: "",
    from: 0,
    to: 0,
  };
};

export const onNewConfig = (state: State, action: Action.WithPayload<Geo.Config>) => {
  return {
    ...state,
    radius: action.payload.radius,
    blur: action.payload.blur,
    zoom: action.payload.zoom,
  };
};

export const onClrConfig = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    radius: 10,
    blur: 10,
    zoom: 8,
  };
};

export const onNewPoints = (state: State, action: Action.WithPayload<Geo.Response[]>) => {
  return {
    ...state,
    points: action.payload.map((e) => { return [e.cityCoord.lat, e.cityCoord.lon]; }),
  };
};

export const onClrPoints = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    points: [],
  };
};
