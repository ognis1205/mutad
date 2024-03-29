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
import createStyles from "@material-ui/core/styles/createStyles";
import * as Themes from "../../../../themes";

const styles = (theme: Themes.Theme): ReturnType<typeof createStyles> =>
  createStyles({
    box: {
      position: theme.pagePosition,
    },
    listItemText: {
      whiteSpace: "nowrap",
      overflow: "hidden",
      textOverflow: "ellipsis",
    },
    text: {
      padding: theme.spacing(2, 2, 0),
    },
    icon: {
      color: "rgb(34, 156, 237)",
      marginRight: 10,
    },
    paper: {
      overflow: "auto",
      height: theme.pageHeight,
      paddingBottom: 50,
    },
    list: {
      marginBottom: theme.spacing(2),
    },
    subheader: {
      backgroundColor: theme.palette.background.paper,
    },
    appBar: {
      position: "absolute",
      top: "auto",
      bottom: 0,
      backgroundColor: theme.drawer.backgroundColor.main,
    },
    grow: {
      flexGrow: 1,
    },
    fabButton: {
      ariaLabel: "add",
      position: "absolute",
      zIndex: 1,
      top: -30,
      left: 0,
      right: 0,
      margin: "0 auto",
      backgroundColor: theme.drawer.backgroundColor.light,
    },
  });

export default styles;
