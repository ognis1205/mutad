/*
 * Copyright 2022 Shingo OKAWA
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
import * as FSA from "typescript-fsa";
import actionCreatorFactory from "typescript-fsa";

const SUFFIX = "geo";

const REQUEST = "request";

const LOAD = "load";

const DONE = "done";

const NEW_QUERY = "new query";

const CLEAR_QUERY = "clear query";

const NEW_CONFIG = "new config";

const CLEAR_CONFIG = "clear config";

const NEW_POINTS = "new points";

const CLEAR_POINTS = "clear points";

export type Point = {
  key: string;
  lon: number;
  lat: number;
  sum: number;
};

export type Query = {
  from: number;
  to: number;
  text: string;
  hashtags: string[];
};

export type Response = {
  cityCoord: {
    lon: number;
    lat: number;
  };
};

export type Config = {
  radius: number;
  blur: number;
  zoom: number;
};

const ACTION_CREATER = actionCreatorFactory(SUFFIX);

export const REQUEST_ACTION = ACTION_CREATER<Query>(REQUEST);

export const LOAD_ACTION = ACTION_CREATER<boolean>(LOAD);

export const DONE_ACTION = ACTION_CREATER<boolean>(DONE);

export const NEW_QUERY_ACTION = ACTION_CREATER<Query>(NEW_QUERY);

export const CLEAR_QUERY_ACTION = ACTION_CREATER<void>(CLEAR_QUERY);

export const NEW_CONFIG_ACTION = ACTION_CREATER<Config>(NEW_CONFIG);

export const CLEAR_CONFIG_ACTION = ACTION_CREATER<void>(CLEAR_CONFIG);

export const NEW_POINTS_ACTION = ACTION_CREATER<Response[]>(NEW_POINTS);

export const CLEAR_POINTS_ACTION = ACTION_CREATER<void>(CLEAR_POINTS);

export const hasAction = (action: FSA.Action<unknown>): boolean =>
  REQUEST_ACTION.match(action) ||
  LOAD_ACTION.match(action) ||
  DONE_ACTION.match(action) ||
  NEW_QUERY_ACTION.match(action) ||
  CLEAR_QUERY_ACTION.match(action) ||
  NEW_CONFIG_ACTION.match(action) ||
  CLEAR_CONFIG_ACTION.match(action) ||
  NEW_POINTS_ACTION.match(action) ||
  CLEAR_POINTS_ACTION.match(action);

export const request = (
  from: number,
  to: number,
  text: string,
  hashtags: string
): FSA.Action<QUERY> =>
  REQUEST_ACTION({
    text: text,
    hashtags: /\S/.test(hashtags) ? hashtags.split(/\s+/) : [],
    from: from,
    to: to,
  });

export const load = (): FSA.Action<boolean> => LOAD_ACTION(true);

export const done = (): FSA.Action<boolean> => DONE_ACTION(false);

export const newQuery = (
  from: number,
  to: number,
  text: string,
  hashtags: string[]
): FSA.Action<Query> =>
  NEW_QUERY_ACTION({
    text: text,
    hashtags: hashtags,
    from: from,
    to: to,
  });

export const clearQuery = (): FSA.Action<void> => CLEAR_QUERY_ACTION();

export const newConfig = (
  radius: number,
  blur: number,
  zoom: number
): FSA.Action<Config> =>
  NEW_CONFIG_ACTION({
    radius: radius,
    blur: blur,
    zoom: zoom,
  });

export const clearConfig = (): FSA.Action<void> => CLEAR_CONFIG_ACTION();

export const newPoints = (json: Response[]): FSA.Action<Response[]> =>
  NEW_POINTS_ACTION(json);

export const clearPoints = (): FSA.Action<void> => CLEAR_CONFIG_ACTION();

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

const INITIAL_STATE = {
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

const reducer = (
  state: State = INITIAL_STATE,
  action: FSA.Action<unknown>
): State => {
  if (FSA.isType(action, LOAD_ACTION)) {
    return {
      ...state,
      loading: true,
    } as State;
  }
  if (FSA.isType(action, DONE_ACTION)) {
    return {
      ...state,
      loading: false,
    } as State;
  }
  if (FSA.isType(action, NEW_QUERY_ACTION)) {
    return {
      ...state,
    } as State;
  }
  if (FSA.isType(action, NEW_DEVICES_ACTION)) {
    return {
      ...state,
      text: action.payload.text,
      hashtags: action.payload.hashtags.join(""),
      from: action.payload.from,
      to: action.payload.to,
    } as State;
  }
  if (FSA.isType(action, CLEAR_QUERY_ACTION)) {
    return {
      ...state,
      text: "",
      hashtags: "",
      from: 0,
      to: 0,
    } as State;
  }
  if (FSA.isType(action, NEW_CONFIG_ACTION)) {
    return {
      ...state,
      radius: action.payload.radius,
      blur: action.payload.blur,
      zoom: action.payload.zoom,
    } as State;
  }
  if (FSA.isType(action, CLEAR_CONFIG_ACTION)) {
    return {
      ...state,
      radius: 10,
      blur: 10,
      zoom: 8,
    } as State;
  }
  if (FSA.isType(action, NEW_POINTS_ACTION)) {
    return {
      ...state,
      points: action.payload.map((p) => [p.cityCoord.lat, p.cityCoord.lon]),
    } as State;
  }
  if (FSA.isType(action, CLEAR_POINTS_ACTION)) {
    return {
      ...state,
      points: [],
    } as State;
  }
  return state;
};

export default reducer;
