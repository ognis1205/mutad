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
import * as Themes from "../../../../themes";

const styles = (
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  theme: Themes.Custom.Theme
): ReturnType<MaterialStyles.createStyles> =>
  MaterialStyles.createStyles({
    form: {
      display: "flex",
      flexWrap: "wrap",
    },
    textField: {
      width: "100%",
    },
  });

export default styles;
