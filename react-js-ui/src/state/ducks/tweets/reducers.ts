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
import { AnyAction } from "redux";
import { fromJS } from "immutable";
import * as types from "./types";

const initState = fromJS({
  latest: [],
});

const onNewLatestTweets = (state, action) => {
  return fromJS({
    latest: action.payload.map((e) => { return {
      user_id: e.userId,
      user_name: e.userName,
      image_url: e.imageUrl,
      text: e.text,
      hashtags: e.hashtags,
      timestamp: e.timestamp,
    }; }),
  });
};

const onAddLatestTweets = (state, action) => {
  return fromJS({
    latest: [...state.get("latest"), ...action.payload.map((e) => { return {
      user_id: e.userId,
      user_name: e.userName,
      image_url: e.imageUrl,
      text: e.text,
      hashtags: e.hashtags,
      timestamp: e.timestamp,
    };})],
  });
};

const onClrLatestTweets = (state, action) => {
  return fromJS({
    latest: [],
  });
};

const reducer = (state = initState, action: AnyAction) => {
  switch (action.type) {
    case types.NEW_LATEST_TWEETS:
      return onNewLatestTweets(state, action);
    case types.ADD_LATEST_TWEETS:
      return onAddLatestTweets(state, action);
    case types.CLR_LATEST_TWEETS:
      return onClrLatestTweets(state, action);
    default:
      return state;
  }
};

export default reducer;
