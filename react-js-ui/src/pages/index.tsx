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
import { NextPage } from "next";
import Specs from "../specs";
import Dashboard from "../components/app/Dashboard";

const Index: NextPage = () => {
  const menu = [].concat(...Specs.menu.map((item, index) => {
    return item.hasOwnProperty("subMenu") && Array.isArray(item.subMenu) ? item.subMenu : [];
  }));

  return (
    <React.Fragment>
      <Dashboard menu={menu} />
    </React.Fragment>
  );
};

export default Index;
