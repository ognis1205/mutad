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

export const useReducer = <R extends React.Reducer<any, any>, State>(
  reducer: R,
  initialState: React.ReducerState<R>,
) => {
  const [state, dispatch] = React.useReducer(reducer, initialState);
  const asyncDispatch = (action: any) => {
    if (typeof action === "function") return action(dispatch);
    return dispatch(action);
  };
  return [state as State, asyncDispatch];
};

export const useContext = <R extends React.Reducer<any, any>, State>(
  initialState: React.ReducerState<R>,
) => {
  const [state, dispatch] = React.useContext(initialState);
  const asyncDispatch = (action: any) => {
    if (typeof action === "function") return action(dispatch);
    return dispatch(action);
  };
  return [state as State, asyncDispatch];
};
