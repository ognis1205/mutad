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
import * as Themes from "../../../themes";

const styles = (theme: Themes.Theme): ReturnType<typeof createStyles> =>
  createStyles({
    grid: {
      height: theme.pageHeight,
      justifyContent: "flex-start",
      padding: "15px",
    },
    card: {
      position: "relative",
      "&:hover, &:focus": {
        top: -5,
      },
    },
    cardMedia: {
      height: 140,
    },
  });

export default styles;
