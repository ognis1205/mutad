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

const SUFFIX = "tweet";

const REQUEST = "init";

const MORE = "more";

const LOAD = "load";

const DONE = "done";

const NEW_QUERY = "new query";

const CLEAR_QUERY = "clear query";

const OPEN = "open";

const CLOSE = "close";

const NEW_LATEST = "new latest";

const ADD_LATEST = "add latest";

const CLEAR_LATEST = "clear latest";

export type Tweet = {
  user_id: string;
  user_name: string;
  image_url: string;
  text: string;
  hashtags: string[];
  timestamp: number;
};

export type Query = {
  before: number;
  text: string;
  hashtags: string[];
  page: number;
  size: number;
};

export type Response = {
  userId: string;
  userName: string;
  imageUrl: string;
  text: string;
  hashtags: string[];
  timestamp: number;
};

const ACTION_CREATER = actionCreatorFactory(SUFFIX);

const REQUEST_ACTION = ACTION_CREATER<Query>(REQUEST);

const MORE_ACTION = ACTION_CREATER<Query>(MORE);

export const LOAD_ACTION = ACTION_CREATER<void>(LOAD);

export const DONE_ACTION = ACTION_CREATER<void>(DONE);

export const NEW_QUERY_ACTION = ACTION_CREATER<Query>(NEW_QUERY);

export const CLEAR_QUERY_ACTION = ACTION_CREATER<void>(CLEAR_QUERY);

export const OPEN_ACTION = ACTION_CREATER<void>(OPEN);

export const CLOSE_ACTION = ACTION_CREATER<void>(CLOSE);

const NEW_LATEST_ACTION = ACTION_CREATER<Response[]>(NEW_LATEST);

const ADD_LATEST_ACTION = ACTION_CREATER<Response[]>(ADD_LATEST);

const CLEAR_LATEST_ACTION = ACTION_CREATER<void>(CLEAR_LATEST);

export const hasAction = (action: FSA.Action<unknown>): boolean =>
  REQUEST_ACTION.match(action) ||
  MORE_ACTION.match(action) ||
  LOAD_ACTION.match(action) ||
  DONE_ACTION.match(action) ||
  NEW_QUERY_ACTION.match(action) ||
  CLEAR_QUERY_ACTION.match(action) ||
  OPEN_ACTION.match(action) ||
  CLOSE_CONFIG_ACTION.match(action) ||
  NEW_LATEST_ACTION.match(action) ||
  ADD_LATEST_ACTION.match(action) ||
  CLEAR_POINTS_ACTION.match(action);

export const request = (
  before: number,
  text: string,
  hashtags: string,
  page: number,
  size: number
): FSA.Action<Query> =>
  REQUEST_ACTION({
    before: before,
    text: text,
    hashtags: /\S/.test(hashtags) ? hashtags.split(/\s+/) : [],
    page: page,
    size: size,
  });

export const more = (
  before: number,
  text: string,
  hashtags: string,
  page: number,
  size: number
): FSA.Action<Query> =>
  MORE_ACTION({
    before: before,
    text: text,
    hashtags: /\S/.test(hashtags) ? hashtags.split(/\s+/) : [],
    page: page,
    size: size,
  });

export const load = (): FSA.Action<void> => LOAD_ACTION();

export const done = (): FSA.Action<void> => DONE_ACTION();

export const newQuery = (query: Query): FSA.Action<Query> =>
  NEW_QUERY_ACTION(query);

export const clearQuery = (): FSA.Action<void> => CLEAR_QUERY_ACTION();

export const open = (): FSA.Action<void> => OPEN_ACTION();

export const close = (): FSA.Action<void> => CLOSE_ACTION();

export const newLatest = (json: Response[]): FSA.Action<Response[]> =>
  NEW_LATEST_ACTION(json);

export const addLatest = (json: Response[]): FSA.Action<Response[]> =>
  ADD_LATEST_ACTION(json);

export const clearLatest = (): FSA.Action<void> => CLEAR_LATEST_ACTION();

export type State = {
  loading: boolean;
  dialog: boolean;
  text: string;
  hashtags: string;
  timestamp: number;
  page: number;
  latest: Tweet[];
};

const INITIAL_STATE = {
  loading: false,
  dialog: false,
  text: "",
  hashtags: "",
  timestamp: 0,
  page: 0,
  latest: [],
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
      timestamp: action.payload.before,
      text: action.payload.text,
      hashtags: action.payload.hashtags.join(""),
      page: action.payload.page,
    } as State;
  }
  if (FSA.isType(action, CLEAR_QUERY_ACTION)) {
    return {
      ...state,
      timestamp: 0,
      text: "",
      hashtags: "",
      page: 0,
    } as State;
  }
  if (FSA.isType(action, OPEN_ACTION)) {
    return {
      ...state,
      dialog: true,
    } as State;
  }
  if (FSA.isType(action, CLOSE_ACTION)) {
    return {
      ...state,
      dialog: false,
    } as State;
  }
  if (FSA.isType(action, NEW_LATEST_ACTION)) {
    return {
      ...state,
      latest: action.payload.map((e) => ({
        user_id: e.userId,
        user_name: e.userName,
        image_url: e.imageUrl,
        text: e.text,
        hashtags: e.hashtags,
        timestamp: e.timestamp,
      })),
    } as State;
  }
  if (FSA.isType(action, ADD_LATEST_ACTION)) {
    return {
      ...state,
      latest: [
        ...state.latest,
        ...action.payload.map((t) => {
          return {
            user_id: t.userId,
            user_name: t.userName,
            image_url: t.imageUrl,
            text: t.text,
            hashtags: t.hashtags,
            timestamp: t.timestamp,
          };
        }),
      ],
    } as State;
  }
  if (FSA.isType(action, CLEAR_LATEST_ACTION)) {
    return {
      ...state,
      latest: [],
    } as State;
  }
  return state;
};

export default reducer;
