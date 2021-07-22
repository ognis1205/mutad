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
import { AnyAction, Store } from "redux";
import { fromJS } from "immutable";
import * as types from "./types";

const initState = fromJS({
  stats: [],
});

const onNewGeoPoints = (state, action) => {
  const count = (geos, classifier) => {
    classifier = classifier || String;
    return geos.reduce((counter, geo) => {
      let key = classifier(geo);
      if (counter.hasOwnProperty(key)) counter[key][2] += 1;
      else counter[key] = [geo.cityCoord.lat, geo.cityCoord.lon, 1];
      return counter;
    }, {})
  };
  let payload = count(action.payload, (geo) => { return geo.cityCoord.lat + "/" + geo.cityCoord.lon; });
  return fromJS({
    stats: [...Object.keys(payload).map(k => payload[k])] || [],
  });
};

const onClrGeoPoints = (state, action) => {
  return fromJS({
    stats: [],
  });
};

const reducer = (state = initState, action: AnyAction) => {
  switch (action.type) {
    case types.NEW_GEO_POINTS:
      return onNewGeoPoints(state, action);
    case types.CLR_GEO_POINTS:
      return onClrGeoPoints(state, action);
    default:
      return state;
  }
};

export default reducer;
