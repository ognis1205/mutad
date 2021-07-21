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
import Assessment from "@material-ui/icons/Assessment";
import BorderClear from "@material-ui/icons/BorderClear";
import BorderOuter from "@material-ui/icons/BorderOuter";
import HomeIcon from "@material-ui/icons/Home";

const data = {
  menus: [
    {
      text: "Twitter Data",
      icon: <Assessment />,
      subMenus: [
        {
          text: "Geoparsing Map",
          description: "Visualization of Tweet Geoparsing",
          image: "/geoparse.jpg",
          icon: <BorderClear />,
          link: "/tweet/map",
        },
        {
          text: "Trend Chart",
          description: "Visualization of Tweet Trend",
          image: "/trend.jpg",
          icon: <BorderOuter />,
          link: "/tweet/trend",
        },
      ],
    },
    {
      text: "Home",
      icon: <HomeIcon />,
      link: "/",
    },
  ],
};

export default data;
