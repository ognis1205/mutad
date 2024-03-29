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
    card: {
      maxWidth: 345,
      position: "absolute",
      zIndex: 800,
      top: 12,
      right: 12,
      boxShadow: "unset",
      border: "2px solid rgba(0,0,0,0.2)",
    },
    cardActions: {
      padding: 0,
    },
    iconButton: {
      transform: "rotate(0deg)",
      marginLeft: "auto",
      transition: theme.transitions.create("transform", {
        duration: theme.transitions.duration.shortest,
      }),
    },
    iconButtonOpen: {
      transform: "rotate(180deg)",
    },
    form: {
      display: "flex",
      flexWrap: "wrap",
    },
    textField: {
      width: "100%",
    },
  });

export default styles;
