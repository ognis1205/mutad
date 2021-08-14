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
import * as Themes from "../../../themes";
import Color from "color";

const styles = (theme: Themes.Custom.Theme) =>
  MaterialStyles.createStyles({
    muMenuItem: {
      color: "white",
      margin: "10px 0",
      fontSize: 14,
      "&:focus": {
        backgroundColor: theme.palette.primary.main,
        "& .MuiListItemIcon-root, & .MuiListItemText-primary": {
          color: theme.palette.common.white,
        },
      },
    },
    listItemIcon: {
      margin: "0 auto",
      color: "white",
      "&:hover": {
        backgroundColor: Color(theme.palette.common.white).alpha(0.5).string(),
      },
      minWidth: "24px",
    },
    muMenu: {
      backgroundColor: "rgb(33, 33, 33)",
      color: "white",
      boxShadow:
        "rgba(0, 0, 0, 0.16) 0px 3px 10px, rgba(0, 0, 0, 0.23) 0px 3px 10px",
    },
    subMuMenuItem: {
      padding: "10px 16px",
      color: "white",
      fontSize: 14,
      "&:focus": {
        backgroundColor: theme.palette.primary.main,
        "& .MuiListItemIcon-root, & .MuiListItemText-primary": {
          color: theme.palette.common.white,
        },
      },
    },
    chevronIcon: {
      float: "right",
      marginLeft: "auto",
    },
    subMenuItem: {
      paddingLeft: 20,
    },
  });

export default styles;
