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
import * as React from "react";
import * as Next from "next";
import dynamic from "next/dynamic";
import Search from "../../../components/tweet/map/Search";

const Index: Next.NextPage = () => {
  const Map = React.useMemo(
    () =>
      dynamic(() => import("../../../components/tweet/map/Map"), {
        // eslint-disable-next-line react/display-name
        loading: () => <p>Loading a map...</p>,
        ssr: false,
      }),
    []
  );

  return (
    <>
      <Map
        center={[0.0, 0.0]}
        zoom={3}
        minZoom={3}
        maxBounds={[
          [-90, -180],
          [90, 180],
        ]}
      />
      <Search />
    </>
  );
};

export default Index;
