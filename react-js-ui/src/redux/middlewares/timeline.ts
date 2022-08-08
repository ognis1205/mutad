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
import * as Redux from "redux";
import * as FSA from "typescript-fsa";
import * as Store from "../store";
import * as Timeline from "../modules/timeline";

export const middleware: Redux.Middleware =
  <S extends Store.Type>({
    dispatch,
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    getState,
  }: Redux.MiddlewareAPI<Redux.Dispatch, S>) =>
  (next: Redux.Dispatch<Redux.AnyAction>) =>
  (action: FSA.Action<unknown>): unknown => {
    if (Timeline.REQUEST_ACTION.match(action)) {
      fetch(`${process.env.API_ENDPOINT}/tweet/latest`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(action.payload),
      })
        .then((res) => {
          if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
          return res.json();
        })
        .then((json) => {
          dispatch(Timeline.newQuery(action.payload));
          dispatch(Timeline.newLatest(json));
          dispatch(Timeline.done());
        })
        .catch((reason) => {
          console.log(reason);
        });
      dispatch(Timeline.load());
    }
    if (Timeline.MORE_ACTION.match(action)) {
      fetch(`${process.env.API_ENDPOINT}/tweet/latest`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(action.payload),
      })
        .then((res) => {
          if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
          return res.json();
        })
        .then((json) => {
          dispatch(Timeline.newQuery(action.payload));
          dispatch(Timeline.addLatest(json));
          dispatch(Timeline.done());
        })
        .catch((reason) => {
          console.log(reason);
        });
      dispatch(Timeline.load());
    }
    return next(action);
  };
