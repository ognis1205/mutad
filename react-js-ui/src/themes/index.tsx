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
import * as MaterialStyles from "@material-ui/core/styles";
import * as MaterialColors from "@material-ui/core/colors";
import * as Custom from "./custom.d";
declare module "@material-ui/core/styles/createTheme" {
  interface ThemeOptions {
    drawer?: any;
  }
}

const defaultTheme = MaterialStyles.createTheme({
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

const customize = (option: any) => {
  return MaterialStyles.createTheme({ ...defaultTheme, ...option });
};

export { defaultTheme, customize, Custom };
