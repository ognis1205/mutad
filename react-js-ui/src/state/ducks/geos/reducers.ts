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
  stats: [],
  radias: 0,
  blur: 0,
  maxZoom: 0,
});

const onNewGeoPoints = (state, action) => {
  return fromJS({
    ...state.toJS(),
    stats: action.payload.map((e) => { return [e.cityCoord.lat, e.cityCoord.lon]; }),
  });
};

const onClrGeoPoints = (state, action) => {
  return fromJS({
    ...state.toJS(),
    stats: [],
  });
};

const onNewGeoRadius = (state, action) => {
  return fromJS({
    ...state.toJS(),
    radius: action.payload,
  });
};

const onNewGeoBlur = (state, action) => {
  return fromJS({
    ...state.toJS(),
    blur: action.payload,
  });
};

const onNewGeoZoom = (state, action) => {
  return fromJS({
    ...state.toJS(),
    zoom: action.payload,
  });
};

const reducer = (state = initState, action: AnyAction) => {
  switch (action.type) {
    case types.NEW_GEO_POINTS:
      return onNewGeoPoints(state, action);
    case types.CLR_GEO_POINTS:
      return onClrGeoPoints(state, action);
    case types.NEW_GEO_RADIUS:
      return onNewGeoRadius(state, action);
    case types.NEW_GEO_BLUR:
      return onNewGeoBlur(state, action);
    case types.NEW_GEO_ZOOM:
      return onNewGeoZoom(state, action);
    default:
      return state;
  }
};

export default reducer;
