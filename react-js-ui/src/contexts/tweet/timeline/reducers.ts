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
  dialog: boolean;
  text: string;
  hashtags: string;
  timestamp: number;
  page: number;
  latest: Tweet.Model[];
};

export const initState = {
  dialog: false,
  text: "",
  hashtags: "",
  timestamp: 0,
  page: 0,
  latest: [],
} as State;

export const onNewText = (state: State, action: Action.WithPayload<string>) => {
  return {
    ...state,
    text: action.payload,
  };
};

export const onClrText = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    text: "",
  };
};

export const onNewHashtags = (state: State, action: Action.WithPayload<string>) => {
  return {
    ...state,
    hashtags: action.payload,
  };
};

export const onClrHashtags = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    hashtags: "",
  };
};

export const onNewTimestamp = (state: State, action: Action.WithPayload<number>) => {
  return {
    ...state,
    timestamp: action.payload,
  };
};

export const onClrTimestamp = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    timestamp: 0,
  };
};

export const onNewPage = (state: State, action: Action.WithPayload<number>) => {
  return {
    ...state,
    page: action.payload,
  };
};

export const onClrPage = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    page: 0,
  };
};

export const onOpnDialog = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    dialog: true,
  };
};

export const onClsDialog = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    dialog: false,
  };
};

export const onNewLatestTweets = (state: State, action: Action.WithPayload<Tweet.Response[]>) => {
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

export const onAddLatestTweets = (state: State, action: Action.WithPayload<Tweet.Response[]>) => {
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

export const onClrLatestTweets = (state: State, _: Action.WithPayload<any>) => {
  return {
    ...state,
    latest: [],
  };
};
