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
import * as Next from "next";
import Hashtags from "../../../components/tweet/trend/Hashtags";
import * as Context from "../../../contexts/tweet/trend";

const Index: Next.NextPage = () => {
  interface ContextProps {
    children: JSX.Element | JSX.Element[];
  }

  const ContextProvider = (props: ContextProps) => {
    const [state, dispatch] = React.useReducer(Context.reducer, Context.init);
    return (
      <Context.store.Provider value={{state, dispatch}}>
        {props.children}
      </Context.store.Provider>
    );
  };

const options = {
  indexAxis: "y",
  responsive: true,
  plugins: {
    legend: {
      display: false,
    },
    title: {
      display: false,
      text: "Hashtags [#]",
    },
  },
};

  return (
    <ContextProvider>
      <Hashtags options={options}/>
    </ContextProvider>
  );
};

export default Index;
