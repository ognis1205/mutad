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
import * as Tweet from "./tweet.d";

export type State = {
  loading: boolean;
  dialog: boolean;
  text: string;
  hashtags: string;
  timestamp: number;
  page: number;
  latest: Tweet.Model[];
};

export const initState = {
  loading: false,
  dialog: false,
  text: "",
  hashtags: "",
  timestamp: 0,
  page: 0,
  latest: [],
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

export const onNewQuery = (state: State, action: Action.WithPayload<Tweet.Query>) => {
  return {
    ...state,
    timestamp: action.payload.before,
    text: action.payload.text,
    hashtags: action.payload.hashtags.join(""),
    page: action.payload.page,
  };
};

export const onClrQuery = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    timestamp: 0,
    text: "",
    hashtags: "",
    page: 0,
  };
};

export const onOpen = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    dialog: true,
  };
};

export const onClose = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    dialog: false,
  };
};

export const onNewLatest = (state: State, action: Action.WithPayload<Tweet.Response[]>) => {
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
  };
};

export const onAddLatest = (state: State, action: Action.WithPayload<Tweet.Response[]>) => {
  return {
    ...state,
    latest: [...state.latest, ...action.payload.map((e) => { return {
      user_id: e.userId,
      user_name: e.userName,
      image_url: e.imageUrl,
      text: e.text,
      hashtags: e.hashtags,
      timestamp: e.timestamp,
    };})],
  };
};

export const onClrLatest = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    latest: [],
  };
};
