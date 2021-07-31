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
import React, { FC } from "react";
import classNames from "classnames";
import {
  Paper,
  Divider,
} from "@material-ui/core";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";

interface Props extends WithStyles<typeof styles> {
  children: FC;
  navDrawerOpen: boolean;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  return (
    <div
      className={classNames(
        props.classes.page,
        !props.navDrawerOpen && props.classes.pageFull
      )}
    >
      <Paper variant="outlined" square>
        <Divider />
        {props.children}
        <div />
      </Paper>
    </div>
  );
});
