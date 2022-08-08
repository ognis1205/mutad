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
import * as NextRedux from "next-redux-wrapper";
import * as Map from "./middlewares/map";
import * as MapModule from "./modules/map";
import * as Timeline from "./middlewares/timeline";
import * as TimelineModule from "./modules/timeline";
import reducer from "./modules/reducer";

const enhancer = Redux.applyMiddleware(Map.middleware, Timeline.middleware);

export type Type = {
  map: MapModule.State;
  timeline: TimelineModule.State;
};

export const wrapper = NextRedux.createWrapper<Redux.Store>(
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  (_: NextRedux.Context) => Redux.createStore(reducer, enhancer),
  { debug: true }
);
