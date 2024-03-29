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
import Assessment from "@material-ui/icons/Assessment";
import BorderClear from "@material-ui/icons/BorderClear";
import BorderInner from "@material-ui/icons/BorderInner";
import BorderOuter from "@material-ui/icons/BorderOuter";
import Home from "@material-ui/icons/Home";

export interface Item {
  text: string;
  icon: React.ReactElement;
  link?: string;
  subMenu?: SubItem[];
}

export interface SubItem {
  text: string;
  description: string;
  image: string;
  icon: React.ReactElement;
  link: string;
}

export const contents = {
  menu: [
    {
      text: "Twitter Data",
      icon: <Assessment />,
      subMenu: [
        {
          text: "Timeline",
          description: "A List Timeline Displays the Latest Tweets",
          image: "/tweet.jpg",
          icon: <BorderClear />,
          link: "/tweet/timeline",
        },
        {
          text: "Heat Map",
          description: "Visualization of Tweet Geoparsing",
          image: "/geoparse.jpg",
          icon: <BorderInner />,
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
      icon: <Home />,
      link: "/",
    },
  ],
};
