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
import * as Action from "../../action.d";
import * as Actions from "./actions";
import * as Reducers from "./reducers";
import * as Types from "./types";
import * as Tweet from "./tweet.d";

type ContextType = {
  state: Reducers.State;
  dispatch: React.Dispatch<Action.WithPayload<any | Tweet.Response[]>>;
};

const store = React.createContext({} as ContextType);

const init = Reducers.initState;

const reducer = (state = init, action: Action.WithPayload<any | string | number | Tweet.Response[]>) => {
  switch (action.type) {
    case Types.ASYNC_LOAD:
      return Reducers.onAsyncLoad(state, action);
    case Types.ASYNC_DONE:
      return Reducers.onAsyncDone(state, action);
    case Types.NEW_QUERY:
      return Reducers.onNewQuery(state, action);
    case Types.CLR_QUERY:
      return Reducers.onClrQuery(state, action);
    case Types.OPN_DIALOG:
      return Reducers.onOpnDialog(state, action);
    case Types.CLS_DIALOG:
      return Reducers.onClsDialog(state, action);
    case Types.NEW_LATEST_TWEETS:
      return Reducers.onNewLatestTweets(state, action);
    case Types.ADD_LATEST_TWEETS:
      return Reducers.onAddLatestTweets(state, action);
    case Types.CLR_LATEST_TWEETS:
      return Reducers.onClrLatestTweets(state, action);
    default:
      return state;
  }
};

export { store, reducer, Tweet, init, Actions };
