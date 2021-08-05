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
import dynamic from "next/dynamic";
import { NextPage } from "next";
import HeatMapSearch from "../../../components/tweet/HeatMapSearch";
import * as Context from "../../../contexts/tweet/map";

const Index: NextPage = () => {
  const HeatMap = React.useMemo(
    () =>
      dynamic(() => import("../../../components/tweet/HeatMap"), {
        loading: () => <p>Loading a map...</p>,
        ssr: false,
      }),
    []
  );

  interface ContextProps {
    children: JSX.Element[];
  }

  const ContextProvider = (props: ContextProps) => {
    const [state, dispatch] = React.useReducer(Context.reducer, Context.init);
    return (
      <Context.store.Provider value={{state, dispatch}}>
        {props.children}
      </Context.store.Provider>
    );
  };

  return (
    <ContextProvider>
      <HeatMap
        center={[0.0, 0.0]}
        zoom={3}
        minZoom={3}
        maxBounds={[
          [-90, -180],
          [90, 180],
        ]}
      />
      <HeatMapSearch/>
    </ContextProvider>
  );
};

export default Index;
