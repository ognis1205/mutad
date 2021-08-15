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
import * as Timeseries from "./timeseries.d";

export type State = {
  timeseriesModel: Timeseries.Model;
};

export const initState = {
  timeseriesModel: null,
} as State;

function timestamp(str: string) {
  const date = new Date(str);
  return date.toLocaleTimeString(undefined, {
    hour: "2-digit",
    minute: "2-digit",
  });
}

export const onNewTimeseries = (
  state: State,
  action: Action.WithPayload<Timeseries.Response[]>
): State => {
  return {
    ...state,
    timeseriesModel: {
      datasets: [
        {
          label: "count",
          data: action.payload.map((e) => {
            return {
              x: timestamp(e.timestamp),
              y: e.count,
            };
          }),
          borderColor: "rgb(  0,  71, 171)",
        },
      ],
    },
  };
};
