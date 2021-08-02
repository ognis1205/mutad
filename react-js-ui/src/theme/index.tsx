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
import { Theme } from "@material-ui/core";
import { createTheme } from "@material-ui/core/styles";
import {
  blue,
  grey,
  pink,
  red,
  deepOrange,
  orange,
  teal,
  green,
  amber,
  lime,
  purple,
  indigo,
} from "@material-ui/core/colors";

declare module "@material-ui/core/styles/createTheme" {
  interface ThemeOptions {
    drawer?: any;
  }
}

export interface CustomTheme extends Theme {
  drawer?: any;
  pageHeight?: string | number;
  pageWidth?: string | number;
  pagePosition?: "relative" | "absolute";
}

export const availableThemes = [
  {
    title: "Default",
    primary: blue,
    secondary: pink,
  },
  {
    title: "Sunset",
    primary: deepOrange,
    secondary: orange,
  },
  {
    title: "Greeny",
    primary: teal,
    secondary: green,
  },
  {
    title: "Beach",
    primary: grey,
    secondary: amber,
  },
  {
    title: "Tech",
    primary: blue,
    secondary: lime,
  },
  {
    title: "Deep Ocean",
    primary: purple,
    secondary: pink,
  },
];

const defaultTheme = {
  palette: {
    primary: indigo,
    secondary: blue,
  },
  error: red,
  appBar: {
    height: 57,
    color: blue[600],
  },
  drawer: {
    width: 240,
    color: grey[900],
    backgroundColor: grey[900],
    miniWidth: 56,
  },
  raisedButton: {
    primaryColor: blue[600],
  },
  typography: {
    //useNextVariants: true
  },
  pageHeight: "87vh",
  pageWidth: "100%",
  pagePosition: "relative",
};

const theme = createTheme(defaultTheme);

export const customTheme = (option: any) => {
  return createTheme({ ...defaultTheme, ...option });
};

export default theme;
