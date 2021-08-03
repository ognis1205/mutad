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
import * as Redux from "react-redux";
import dynamic from "next/dynamic";
import { NextPage } from "next";
import HeatMapSearch from "../../../components/tweet/HeatMapSearch";
import {
  Actions,
} from "../../../state/ducks/geos";

const Index: NextPage = () => {
  const HeatMap = React.useMemo(
    () =>
      dynamic(() => import("../../../components/tweet/HeatMap"), {
        loading: () => <p>Loading a map...</p>,
        ssr: false,
      }),
    []
  );

  const dispatch = Redux.useDispatch();

  const [text, setText] = React.useState<string>("");

  const [hashtags, setHashtags] = React.useState<string>("");

  const [from, setFrom] = React.useState<number>((new Date()).getTime());

  const [to, setTo] = React.useState<number>((new Date()).getTime());

  const handleText = (text: string) => {
    setText(text);
  };

  const handleHashtags = (hashtags: string) => {
    setHashtags(hashtags);
  };

  const handleFrom = (from: number) => {
    setFrom(from);
  };

  const handleTo = (to: number) => {
    setTo(to);
  };

  const toHashtags = (hashtags: string) => (
    /\S/.test(hashtags) ? hashtags.split(/\s+/) : []
  );

  const handlePost = async () => {
    dispatch(Actions.reqGeoPoints({
      from: from,
      to: to,
      text: text,
      hashtags: toHashtags(hashtags),
    }));
  }

  return (
    <React.Fragment>
      <HeatMap
        center={[0.0, 0.0]}
        zoom={3}
        minZoom={3}
        maxBounds={[
          [-90, -180],
          [90, 180],
        ]}
      />
      <HeatMapSearch
        onText={handleText}
        onHashtags={handleHashtags}
        onFrom={handleFrom}
        onTo={handleTo}
        onPost={handlePost}
      />
    </React.Fragment>
  );
};

export default Index;
