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
import { createStyles } from "@material-ui/core/styles";

export const styles = (theme: any) =>
  createStyles({
    logo: {
      cursor: "pointer",
      fontSize: 22,
      color: "white",
      lineHeight: "64px",
      fontWeight: 300,
      backgroundColor: theme.palette.primary[500],
      paddingLeft: 40,
      height: 64,
    },
    drawer: {
      width: theme.drawer.width,
      backgroundColor: "rgb(33, 33, 33)",
      color: "white",
      borderRight: "0px",
      boxShadow:
        "rgba(0, 0, 0, 0.16) 0px 3px 10px, rgba(0, 0, 0, 0.23) 0px 3px 10px",
    },
    drawerClose: {
      overflowX: "hidden",
      transition: theme.transitions.create("width", {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      width: theme.drawer.miniWidth,
    },
  });
