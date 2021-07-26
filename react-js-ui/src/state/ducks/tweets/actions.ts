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
import { TweetQuery } from "./tweet.d";
import { NEW_LATEST_TWEETS, CLR_LATEST_TWEETS } from "./types";

export const reqLatestTweets = (query: TweetQuery) => async (dispatch, getState) => {
    const opts = {
      method: "POST",
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(query),
    };
    fetch(`${process.env.API_ENDPOINT}/tweet/latest`, opts)
    .then((res) => {
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      return res.json();
    })
    .then((json) => {
      dispatch(newLatestTweets(json))
    })
    .catch((reason) => {
      console.log(reason);
    });
};

export const newLatestTweets = (json) => ({
  type: NEW_LATEST_TWEETS,
  payload: json,
});

export const clrLatestTweets = () => ({
  type: CLR_LATEST_TWEETS,
  payload: [],
});
