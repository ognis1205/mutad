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
import { PayloadAction } from '@reduxjs/toolkit';
import { GeoResponse } from "./geo.d";
import * as types from "./types";

export type GeoState = {
  stats: number[][];
  radius: number;
  blur: number;
  zoom: number;
};

const initState: GeoState = {
  stats: [],
  radius: 10,
  blur: 10,
  zoom: 8,
};

const onNewGeoPoints = (state: GeoState, action: PayloadAction<GeoResponse[]>) => {
  return {
    ...state,
    stats: action.payload.map((e) => { return [e.cityCoord.lat, e.cityCoord.lon]; }),
  };
};

const onClrGeoPoints = (state: GeoState, action: PayloadAction<any>) => {
  return {
    ...state,
    stats: [],
  };
};

const onNewGeoRadius = (state: GeoState, action: PayloadAction<number>) => {
  return {
    ...state,
    radius: action.payload,
  };
};

const onNewGeoBlur = (state: GeoState, action: PayloadAction<number>) => {
  return {
    ...state,
    blur: action.payload,
  };
};

const onNewGeoZoom = (state: GeoState, action: PayloadAction<number>) => {
  return {
    ...state,
    zoom: action.payload,
  };
};

const reducer = (state = initState, action: PayloadAction<any | number | GeoResponse[]>) => {
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
