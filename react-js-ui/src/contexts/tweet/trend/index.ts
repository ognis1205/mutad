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
import * as Topic from "./topic.d";
import * as Timeseries from "./timeseries.d";

type ContextType = {
  state: Reducers.State;
  dispatch: React.Dispatch<Action.WithPayload<any | Topic.Response[] | Timeseries.Response[]>>;
};

const store = React.createContext({} as ContextType);

const init = Reducers.initState;

const reducer = (state = init, action: Action.WithPayload<any | string | number | Topic.Response[] | Timeseries.Response[]>) => {
  switch (action.type) {
    case Types.LOAD:
      return Reducers.onLoad(state, action);
    case Types.DONE:
      return Reducers.onDone(state, action);
    case Types.NEW_TOPIC:
      return Reducers.onNewTopic(state, action);
    case Types.CLR_TOPIC:
      return Reducers.onClrTopic(state, action);
    case Types.NEW_TIMESERIES:
      return Reducers.onNewTimeseries(state, action);
    case Types.CLR_TIMESERIES:
      return Reducers.onClrTimeseries(state, action);
    default:
      return state;
  }
};

export { store, reducer, Topic, Timeseries, init, Actions };
