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
import { GeoQuery } from "./geo.d";
import { NEW_GEO_POINTS, CLR_GEO_POINTS } from "./types";

export const reqGeoPoints = (query: GeoQuery) => async (dispatch, getState) => {
    const opts = {
      method: "POST",
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(query),
    };
    fetch(`${process.env.API_ENDPOINT}/geo/hashtags`, opts)
    .then((res) => {
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
      return res.json();
    })
    .then((json) => {
      dispatch(newGeoPoints(json))
    })
    .catch((reason) => {
      console.log(reason);
    });
};

export const newGeoPoints = (json) => ({
  type: NEW_GEO_POINTS,
  payload: json,
});

export const clrGeoPoints = () => ({
  type: CLR_GEO_POINTS,
  payload: [],
});
