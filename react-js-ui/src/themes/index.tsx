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
import * as Material from "@material-ui/core";
import * as MaterialStyles from "@material-ui/core/styles";
import * as MaterialColors from "@material-ui/core/colors";

declare module "@material-ui/core/styles/createTheme" {
  interface ThemeOptions {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    drawer?: any;
  }
}

export interface Theme extends Material.Theme {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  drawer?: any;
  pageHeight?: string | number;
  pageWidth?: string | number;
  pagePosition?: "relative" | "absolute";
}

export const defaultTheme = MaterialStyles.createTheme({
  palette: {
    primary: MaterialColors.indigo,
    secondary: MaterialColors.blue,
  },
  error: MaterialColors.red,
  appBar: {
    height: 57,
    color: MaterialColors.blue[600],
  },
  drawer: {
    width: 240,
    color: MaterialColors.grey[900],
    backgroundColor: {
      main: MaterialColors.grey[900],
      light: MaterialColors.grey[300],
    },
    miniWidth: 56,
  },
  raisedButton: {
    primaryColor: MaterialColors.blue[600],
  },
  pageHeight: "87vh",
  pageWidth: "100%",
  pagePosition: "relative",
} as Custom.Theme);

export const customize = (
  option: ThemeOptions
): ReturnType<MaterialStyles.createTheme> => {
  return MaterialStyles.createTheme({ ...defaultTheme, ...option });
};
